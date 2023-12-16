package com.andreikslpv.flickrrecent.domain.usecase.settings

import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import javax.inject.Inject

class ObserveNotificationSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke() =
        settingsRepository.getSettingBooleanValueFlow(SettingsBooleanType.NOTIFICATION)
}