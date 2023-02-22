package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    fun getRecentPhoto(): Flow<ApiResult<PhotoDomainModel>>

    fun addPhotoInFavorites(photo: PhotoDomainModel)

}