package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository

class RefreshRecentPhotoUseCase(private val photosRepository: PhotosRepository) {
    fun execute() {
        return photosRepository.refreshRecentPhoto()
    }
}