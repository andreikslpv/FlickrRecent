package com.andreikslpv.flickrrecent.domain.usecase.settings

import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingNotification
import javax.inject.Inject

class InverseNotificationSettingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke() {
        val newValue = !(settingsRepository.getSettingValue(SettingNotification()) as Boolean)
        settingsRepository.putSettingValue(SettingNotification(), newValue)
    }
}