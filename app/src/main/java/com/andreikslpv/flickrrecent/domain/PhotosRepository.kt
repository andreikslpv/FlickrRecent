package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.MutableStateFlow

interface PhotosRepository {
    fun getRecentPhoto(): MutableStateFlow<ApiResult<PhotoDomainModel>>

    fun refreshRecentPhoto()

    fun addPhotoInFavorites(photo: PhotoDomainModel)

}