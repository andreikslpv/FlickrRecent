package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType

class InverseBooleanSettingValueUseCase(
    private val settingsRepository: SettingsRepository,
    private val photosRepository: PhotosRepository
) {
    fun execute(setting: SettingsBooleanType) {
        val newValue = !settingsRepository.getSettingBooleanValue(setting)
        settingsRepository.setSettingBooleanValue(setting, newValue)

        if (setting == SettingsBooleanType.NOTIFICATION)
            photosRepository.setNotificationStatus(newValue)
    }
}