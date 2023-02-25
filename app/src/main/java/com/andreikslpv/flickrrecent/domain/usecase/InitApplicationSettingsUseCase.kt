package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType

class InitApplicationSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val photosRepository: PhotosRepository
) {
    fun execute() {
        photosRepository.setNotificationStatus(
            settingsRepository.getSettingBooleanValue(
                SettingsBooleanType.NOTIFICATION
            )
        )
    }
}