package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.andreikslpv.flickrrecent.data.api.DtoToDomainMapper
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.data.db.*
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


const val NAME_OF_CACHE = "lastCachedPhoto.png"
const val UPDATE_TIME_INTERVAL = 60000L

class PhotosRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi,
    private val realmDb: Realm,
    private val context: Context,
) : PhotosRepository {
    private val start: ApiResult<PhotoDomainModel> = ApiResult.Loading(null, true)
    private val _currentResult = MutableStateFlow(start)
    private val _currentPhotoStatus = MutableStateFlow(false)
    private val _currentNotificationStatus = MutableStateFlow(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(UPDATE_TIME_INTERVAL)
                refreshRecentPhoto()
                if (_currentNotificationStatus.value) println("I/o notification")
            }
        }
    }

    override fun setNotificationStatus(enable: Boolean) {
        _currentNotificationStatus.tryEmit(enable)
    }

    override fun getNotificationStatus(): MutableStateFlow<Boolean> {
        return _currentNotificationStatus
    }

    override fun getRecentPhoto(): MutableStateFlow<ApiResult<PhotoDomainModel>> {
        return _currentResult
    }

    override fun refreshRecentPhoto() {
        _currentPhotoStatus.tryEmit(false)
        CoroutineScope(EmptyCoroutineContext).launch {
            _currentResult.tryEmit(ApiResult.Loading(null, true))

            try {
                val response = flickrApi.getPhotos()
                if (response.isSuccessful) {
                    val photos = response.body()?.photos?.photo
                    if (photos.isNullOrEmpty()) {
                        _currentResult.tryEmit(ApiResult.Error("unknown error"))
                    } else {
                        _currentResult.tryEmit(ApiResult.Success(DtoToDomainMapper.map(photos[0])))
                        savePhotoToDisk()
                        savePhotoToCache()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    response.errorBody()?.close()
                    _currentResult.tryEmit(ApiResult.Error(errorMsg))
                }
            } catch (e: Exception) {
                _currentResult.tryEmit(ApiResult.Error(e.message ?: "unknown error"))
            }

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

    override suspend fun savePhotoToCache() {
        realmDb.write {
            val cachedPhoto = DomainToCacheMapper.map(_currentResult.value.data)
            copyToRealm(cachedPhoto, UpdatePolicy.ALL)
        }
    }

    override suspend fun savePhotoToDisk() {
        val job = CoroutineScope(Dispatchers.IO).async {
            _currentResult.value.data?.let { loadImage(it.linkBigPhoto) }
        }
        job.await()?.let { convertBitmapToFile(it) }
    }

    override fun loadPhotoFromCache() {
        val photo =
            realmDb.query<PhotoCacheModel>("id = $0", "1").find().firstOrNull() ?: return
        CoroutineScope(EmptyCoroutineContext).launch {
            _currentResult.tryEmit(ApiResult.Loading(null, true))
            _currentResult.tryEmit(ApiResult.Cache(CacheToDomainMapper.map(photo)))
        }
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