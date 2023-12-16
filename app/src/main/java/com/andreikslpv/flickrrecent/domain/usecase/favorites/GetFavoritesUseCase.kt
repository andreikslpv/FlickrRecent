package com.andreikslpv.flickrrecent.domain.usecase.favorites

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val photosRepository: PhotosRepository) {

    operator fun invoke() = photosRepository.getFavoritesFlow()
}

