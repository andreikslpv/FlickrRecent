package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import kotlinx.coroutines.flow.MutableStateFlow

class GetNotificationStatusUseCase(private val photosRepository: PhotosRepository) {
    fun execute(): MutableStateFlow<Boolean> {
        return photosRepository.getNotificationStatus()
    }
}