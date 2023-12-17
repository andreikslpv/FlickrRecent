package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.InverseNotificationSettingUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.ObserveNotificationSettingsUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class MainViewModel @AssistedInject constructor(
    private val setActivityRunningStatusUseCase: SetActivityRunningStatusUseCase,
    private val inverseNotificationSettingUseCase: InverseNotificationSettingUseCase,
    observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase,
) : ViewModel() {

    val notificationSetting = observeNotificationSettingsUseCase().asLiveData()

    fun inverseNotificationSetting() = inverseNotificationSettingUseCase()

    fun setActivityStatus(isRunning: Boolean) = setActivityRunningStatusUseCase(isRunning)

    @AssistedFactory
    interface Factory {
        fun create(): MainViewModel
    }
}


