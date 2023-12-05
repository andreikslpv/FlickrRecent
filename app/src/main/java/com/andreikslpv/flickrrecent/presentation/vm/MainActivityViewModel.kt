package com.andreikslpv.flickrrecent.presentation.vm

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import kotlinx.coroutines.*
import javax.inject.Inject


@SuppressLint("UnspecifiedImmutableFlag")
class MainActivityViewModel : ViewModel() {

    @Inject
    lateinit var setActivityRunningStatusUseCase: SetActivityRunningStatusUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setActivityStatus(isRunning: Boolean) = setActivityRunningStatusUseCase(isRunning)
}


