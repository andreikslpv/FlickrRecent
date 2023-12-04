package com.andreikslpv.flickrrecent.domain.usecase.notification

import com.andreikslpv.flickrrecent.domain.NotificationRepository

class SetActivityRunningStatusUseCase(private val notificationRepository: NotificationRepository) {

    operator fun invoke(isRunning: Boolean) =
        notificationRepository.setActivityRunningStatus(isRunning)
}