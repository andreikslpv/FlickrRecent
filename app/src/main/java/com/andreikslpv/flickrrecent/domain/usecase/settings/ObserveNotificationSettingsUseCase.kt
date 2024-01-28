package com.andreikslpv.flickrrecent.domain.usecase.settings

import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingNotification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotificationSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke() =
        settingsRepository.observeSetting(SettingNotification()) as Flow<Boolean>
}