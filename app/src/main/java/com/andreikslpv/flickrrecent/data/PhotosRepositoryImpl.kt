package com.andreikslpv.flickrrecent.data

import com.andreikslpv.flickrrecent.data.api.DtoToDomainMapper
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi
) : PhotosRepository {

    override fun getRecentPhoto(): Flow<ApiResult<PhotoDomainModel>> {
        return flow {
            emit(ApiResult.Loading(null, true))
            val response = flickrApi.getPhotos()

            if (response.isSuccessful) {
                println("I/o ${response.body()?.stat}")
                val photos = response.body()?.photos?.photo
                println("I/o $photos")
                if (photos.isNullOrEmpty()) emit(ApiResult.Error(""))
                else emit(ApiResult.Success(DtoToDomainMapper.map(photos[0])))
            } else {
                val errorMsg = response.errorBody()?.string() ?: ""
                response.errorBody()?.close()
                emit(ApiResult.Error(errorMsg))
            }
        }
    }

    override fun addPhotoInFavorites(photo: PhotoDomainModel) {
        TODO("Not yet implemented")
    }

}