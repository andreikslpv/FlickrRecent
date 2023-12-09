package com.andreikslpv.flickrrecent.domain.usecase.favorites

import com.andreikslpv.flickrrecent.domain.PhotosRepository

class RemovePhotoFromFavoritesUseCase(private val photosRepository: PhotosRepository) {

    suspend operator fun invoke(photoId: String) =
        photosRepository.removePhotoFromFavorites(photoId)
}