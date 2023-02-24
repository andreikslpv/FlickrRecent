package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val photosRepository: PhotosRepository) {
    fun execute(): Flow<List<PhotoDomainModel>> {
        return photosRepository.getFavorites()
    }
}

