package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import javax.inject.Inject


class MainViewModel : ViewModel() {

    @Inject
    lateinit var setActivityRunningStatusUseCase: SetActivityRunningStatusUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setActivityStatus(isRunning: Boolean) = setActivityRunningStatusUseCase(isRunning)
}


