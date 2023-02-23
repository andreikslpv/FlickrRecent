package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.MutableStateFlow

class GetRecentPhotoUseCase(private val photosRepository: PhotosRepository) {
    fun execute(): MutableStateFlow<ApiResult<PhotoDomainModel>> {
        return photosRepository.getRecentPhoto()
    }
}