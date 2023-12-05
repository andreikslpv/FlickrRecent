package com.andreikslpv.flickrrecent.domain.usecase.settings

import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType

class ObserveNotificationSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke() =
        settingsRepository.getSettingBooleanValueFlow(SettingsBooleanType.NOTIFICATION)
}