package com.andreikslpv.flickrrecent.domain.usecase.notification

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import javax.inject.Inject

class SetActivityRunningStatusUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {

    operator fun invoke(isRunning: Boolean) =
        notificationRepository.setActivityRunningStatus(isRunning)
}