package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository

class RemovePhotoFromFavoritesUseCase(private val photosRepository: PhotosRepository) {
    suspend fun execute(photoId: String) {
        photosRepository.removePhotoFromFavorites(photoId)
    }
}