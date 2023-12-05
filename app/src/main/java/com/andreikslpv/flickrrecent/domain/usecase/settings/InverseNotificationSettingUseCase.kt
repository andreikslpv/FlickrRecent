package com.andreikslpv.flickrrecent.domain.usecase.settings

import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType

class InverseNotificationSettingUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke() {
        val newValue = !settingsRepository.getSettingBooleanValue(SettingsBooleanType.NOTIFICATION)
        settingsRepository.setSettingBooleanValue(SettingsBooleanType.NOTIFICATION, newValue)
    }
}