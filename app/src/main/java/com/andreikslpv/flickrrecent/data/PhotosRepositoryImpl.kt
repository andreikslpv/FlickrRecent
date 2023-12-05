package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.andreikslpv.flickrrecent.data.api.DtoToDomainMapper
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.data.db.CacheToDomainMapper
import com.andreikslpv.flickrrecent.data.db.DomainToCacheMapper
import com.andreikslpv.flickrrecent.data.db.DomainToRealmMapper
import com.andreikslpv.flickrrecent.data.db.PhotoRealmModel
import com.andreikslpv.flickrrecent.data.db.RealmToDomainListMapper
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


const val NAME_OF_CACHE = "lastCachedPhoto.png"

class PhotosRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi,
    private val realmDb: Realm,
    private val context: Context,
) : PhotosRepository {
    private val _currentPhotoStatus = MutableStateFlow(false)

    override fun getRecentPhoto() = flow {
        emit(Response.Loading)
        try {
            val response = flickrApi.getPhotos()
            if (response.isSuccessful) {
                val photos = response.body()?.photos?.photo
                if (photos.isNullOrEmpty()) {
                    emit(Response.Failure(Throwable("unknown error")))
                } else {
                    val photo = DtoToDomainMapper.map(photos[0])
                    emit(Response.Success(photo))
                    savePhotoToDisk(photo)
                    savePhotoToCache(photo)
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: ""
                response.errorBody()?.close()
                emit(Response.Failure(Throwable(errorMsg)))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override fun getPhotoFromCache() = flow {
        emit(Response.Loading)
        val photo =
            realmDb.query<PhotoCacheModel>("id = $0", "1").find().firstOrNull()
        if (photo != null) {
            emit(Response.Success(CacheToDomainMapper.map(photo)))
        } else {
            emit(Response.Failure(Throwable("Cache is empty")))
        }
    }

    override suspend fun addPhotoInFavorites(photo: PhotoDomainModel) {
        realmDb.write {
            copyToRealm(DomainToRealmMapper.map(photo))
        }
        _currentPhotoStatus.tryEmit(true)
    }

    override suspend fun removePhotoFromFavorites(photoId: String) {
        realmDb.write {
            val query = this.query<PhotoRealmModel>("id = $0", photoId)
            delete(query)
        }
        _currentPhotoStatus.tryEmit(false)
    }

    override fun getPhotoStatus(): MutableStateFlow<Boolean> {
        return _currentPhotoStatus
    }

    override fun isPhotoFavorites(photoId: String): Boolean {
        val photo: RealmResults<PhotoRealmModel> =
            realmDb.query<PhotoRealmModel>("id = $0", photoId).find()
        return !photo.isEmpty()
    }

    override fun getFavorites(): Flow<List<PhotoDomainModel>> {
        return realmDb.query<PhotoRealmModel>()
            .asFlow()
            .map {
                RealmToDomainListMapper.map(it.list)
            }
    }

    private suspend fun savePhotoToCache(photo: PhotoDomainModel) {
        realmDb.write {
            val cachedPhoto = DomainToCacheMapper.map(photo)
            copyToRealm(cachedPhoto, UpdatePolicy.ALL)
        }
    }

    private suspend fun savePhotoToDisk(photo: PhotoDomainModel) {
        val job = CoroutineScope(Dispatchers.IO).async {
            loadImage(photo.linkBigPhoto)
        }
        convertBitmapToFile(job.await())
    }

    private suspend fun loadImage(url: String): Bitmap {
        return suspendCoroutine {
            Executors.newSingleThreadExecutor().execute {
                val bitmap = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
                it.resume(bitmap)
            }
        }
    }

    private fun convertBitmapToFile(bitmap: Bitmap) {
        val dest = File("${context.filesDir}${File.separator}$NAME_OF_CACHE")
        try {
            val out = FileOutputStream(dest)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}