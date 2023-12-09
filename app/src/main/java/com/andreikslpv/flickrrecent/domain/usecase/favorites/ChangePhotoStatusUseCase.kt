package com.andreikslpv.flickrrecent.domain.usecase.favorites

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePhotoStatusUseCase(private val photosRepository: PhotosRepository) {

    operator fun invoke(photo: PhotoDomainModel) {
        CoroutineScope(Dispatchers.IO).launch {
            if (photosRepository.getFavoritesIds().contains(photo.id))
                photosRepository.removePhotoFromFavorites(photo.id)
            else photosRepository.addPhotoInFavorites(photo)
        }
    }
}