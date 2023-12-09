package com.andreikslpv.flickrrecent.domain.usecase.favorites

import com.andreikslpv.flickrrecent.domain.PhotosRepository

class GetFavoritesUseCase(private val photosRepository: PhotosRepository) {

    operator fun invoke() = photosRepository.getFavoritesFlow()
}

