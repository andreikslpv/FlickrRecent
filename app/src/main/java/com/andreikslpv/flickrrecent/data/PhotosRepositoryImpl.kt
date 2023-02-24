package com.andreikslpv.flickrrecent.data

import com.andreikslpv.flickrrecent.data.api.DtoToDomainMapper
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.db.DomainToRealmMapper
import com.andreikslpv.flickrrecent.data.db.PhotoRealmModel
import com.andreikslpv.flickrrecent.data.db.RealmToDomainListMapper
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class PhotosRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi,
    private val realmDb: Realm,
) : PhotosRepository {
    private val start: ApiResult<PhotoDomainModel> = ApiResult.Loading(null, true)
    private val _currentResult = MutableStateFlow(start)
    private val _currentPhotoStatus = MutableStateFlow(false)

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
                    if (photos.isNullOrEmpty()) _currentResult.tryEmit(ApiResult.Error("unknown error"))
                    else _currentResult.tryEmit(ApiResult.Success(DtoToDomainMapper.map(photos[0])))
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

}