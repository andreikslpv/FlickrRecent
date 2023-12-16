package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class MainViewModel @AssistedInject constructor(
    private val setActivityRunningStatusUseCase: SetActivityRunningStatusUseCase
) : ViewModel() {

    fun setActivityStatus(isRunning: Boolean) = setActivityRunningStatusUseCase(isRunning)

    @AssistedFactory
    interface Factory {
        fun create(): MainViewModel
    }
}


