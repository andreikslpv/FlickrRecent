package com.andreikslpv.flickrrecent.domain.usecase.notification

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.NotificationStatus
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import javax.inject.Inject

class GetNotificationStatusUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(): NotificationStatus {
        return if (!settingsRepository.getSettingBooleanValue(SettingsBooleanType.NOTIFICATION)) {
            NotificationStatus.NOTIFICATION_DISABLED
        } else {
            if (notificationRepository.getActivityRunningStatus()) NotificationStatus.NOTIFICATION_ENABLED_AND_NOT_SHOWING
            else NotificationStatus.NOTIFICATION_ENABLED_AND_SHOWING

        }

    }
}