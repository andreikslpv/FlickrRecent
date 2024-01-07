package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.api.FlickrToDomainMapper
import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.data.db.CacheToDomainMapper
import com.andreikslpv.flickrrecent.data.db.DomainToCacheMapper
import com.andreikslpv.flickrrecent.data.db.DomainToRealmMapper
import com.andreikslpv.flickrrecent.data.db.PhotoRealmModel
import com.andreikslpv.flickrrecent.data.db.RealmToDomainListMapper
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.EmptyCacheException
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.models.UnknownException
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


const val NAME_OF_CACHE = "lastCachedPhoto.png"

class PhotosRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val realmDb: Realm,
    private val context: Context,
) : PhotosRepository {

    private val flickrApi = retrofit.create(FlickrApi::class.java)

    override fun getRecentPhoto() = flow {
        emit(Response.Loading)
        try {
            val response = flickrApi.getPhotos()
            if (response.isSuccessful) {
                val photo = FlickrToDomainMapper.map(response.body())
                if (photo != PhotoDomainModel()) {
                    emit(Response.Success(photo))
                    savePhotoToCache(photo)
                } else {
                    emit(Response.Failure(UnknownException()))
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
        val photo = realmDb.query<PhotoCacheModel>("id = $0", "1").find().firstOrNull()
        if (photo != null) {
            emit(Response.Success(CacheToDomainMapper.map(photo)))
        } else {
            emit(Response.Failure(EmptyCacheException()))
        }
    }

    override suspend fun addPhotoInFavorites(photo: PhotoDomainModel) {
        realmDb.write {
            copyToRealm(DomainToRealmMapper.map(photo))
        }
    }

    override suspend fun removePhotoFromFavorites(photoId: String) {
        realmDb.write {
            val query = this.query<PhotoRealmModel>("id = $0", photoId)
            delete(query)
        }
    }

    override fun getFavoritesIds() = realmDb.query<PhotoRealmModel>()
        .find()
        .toList()
        .map { it.id }

    override fun getFavoritesFlow() = realmDb.query<PhotoRealmModel>()
        .asFlow()
        .map { RealmToDomainListMapper.map(it.list) }


    private suspend fun savePhotoToCache(photo: PhotoDomainModel) {
        val job = CoroutineScope(Dispatchers.IO).async {
            loadImage(photo.linkBigPhoto)
        }
        val bitmap = job.await()
        bitmap?.let {
            val newUri = convertBitmapToFile(it).toString()
            photo.linkBigPhoto = newUri
            photo.linkSmallPhoto = newUri
        }

        realmDb.write {
            val cachedPhoto = DomainToCacheMapper.map(photo)
            copyToRealm(cachedPhoto, UpdatePolicy.ALL)
        }
    }

    private suspend fun loadImage(url: String): Bitmap? {
        return suspendCoroutine {
            Executors.newSingleThreadExecutor().execute {
                val bitmap = try {
                    BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
                } catch (e: Exception) {
                    null
                }
                it.resume(bitmap)
            }
        }
    }

    private fun convertBitmapToFile(bitmap: Bitmap): Uri {
        val dest = File("${context.filesDir}${File.separator}$NAME_OF_CACHE")
        return try {
            val out = FileOutputStream(dest)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            dest.toUri()
        } catch (e: Exception) {
            e.printStackTrace()
            Uri.parse("")
        }
    }
}