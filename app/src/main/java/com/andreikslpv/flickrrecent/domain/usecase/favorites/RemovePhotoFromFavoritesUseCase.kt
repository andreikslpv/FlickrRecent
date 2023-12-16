package com.andreikslpv.flickrrecent.domain.usecase.favorites

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import javax.inject.Inject

class RemovePhotoFromFavoritesUseCase @Inject constructor(private val photosRepository: PhotosRepository) {

    suspend operator fun invoke(photoId: String) =
        photosRepository.removePhotoFromFavorites(photoId)
}