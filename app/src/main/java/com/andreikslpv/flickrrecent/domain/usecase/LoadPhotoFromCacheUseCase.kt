package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository

class LoadPhotoFromCacheUseCase(private val photosRepository: PhotosRepository) {
    fun execute() {
        return photosRepository.loadPhotoFromCache()
    }
}