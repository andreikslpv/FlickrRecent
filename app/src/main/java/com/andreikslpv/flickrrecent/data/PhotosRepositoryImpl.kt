package com.andreikslpv.flickrrecent.data

import com.andreikslpv.flickrrecent.data.api.DtoToDomainMapper
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class PhotosRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi
) : PhotosRepository {
    private val start: ApiResult<PhotoDomainModel> = ApiResult.Loading(null, true)
    private val _currentResult = MutableStateFlow(start)

    override fun getRecentPhoto(): MutableStateFlow<ApiResult<PhotoDomainModel>> {
        return _currentResult
    }

    override fun refreshRecentPhoto() {
        CoroutineScope(EmptyCoroutineContext).launch {
            _currentResult.tryEmit(ApiResult.Loading(null, true))
            val response = flickrApi.getPhotos()

            if (response.isSuccessful) {
                println("I/o ${response.body()?.stat}")
                val photos = response.body()?.photos?.photo
                println("I/o $photos")
                if (photos.isNullOrEmpty()) _currentResult.tryEmit(ApiResult.Error(""))
                else _currentResult.tryEmit(ApiResult.Success(DtoToDomainMapper.map(photos[0])))
            } else {
                val errorMsg = response.errorBody()?.string() ?: ""
                response.errorBody()?.close()
                _currentResult.tryEmit(ApiResult.Error(errorMsg))
            }
        }
    }

    override fun addPhotoInFavorites(photo: PhotoDomainModel) {
        TODO("Not yet implemented")
    }

}