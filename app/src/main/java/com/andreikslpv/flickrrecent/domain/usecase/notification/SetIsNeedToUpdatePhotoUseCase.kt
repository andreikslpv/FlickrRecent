package com.andreikslpv.flickrrecent.domain.usecase.notification

import com.andreikslpv.flickrrecent.domain.NotificationRepository

class SetIsNeedToUpdatePhotoUseCase(private val notificationRepository: NotificationRepository) {

    operator fun invoke() =
        notificationRepository.setIsNeedToUpdatePhoto()
}