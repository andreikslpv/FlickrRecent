package com.andreikslpv.flickrrecent.presentation.vm

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.usecase.InitApplicationSettingsUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import kotlinx.coroutines.*
import javax.inject.Inject


@SuppressLint("UnspecifiedImmutableFlag")
class MainActivityViewModel : ViewModel() {

//    private val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    //private val notifyPendingIntent: PendingIntent
//    private val requestCode = 0
//    private val notifyIntent = Intent(App.instance, AlarmReceiver::class.java)
//    private var triggerTime: Long = 0L
//
//    // false когда активити running, true - во всех остальных состояниях ЖЦ
//    private var isNotRunning = false
//
//    private val notificationStatusFlow: StateFlow<Boolean> by lazy {
//        getNotificationStatusUseCase.execute().asStateFlow()
//    }
//
//    private val photoStateFlow: StateFlow<ApiResult<PhotoDomainModel>> by lazy {
//        getRecentPhotoUseCase.execute().asStateFlow()
//    }
//
//    @Inject
//    lateinit var getNotificationStatusUseCase: GetNotificationStatusUseCase
//
//    @Inject
//    lateinit var refreshRecentPhotoUseCase: RefreshRecentPhotoUseCase
//
//    @Inject
//    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase

    @Inject
    lateinit var setActivityRunningStatusUseCase: SetActivityRunningStatusUseCase

    @Inject
    lateinit var initApplicationSettingsUseCase: InitApplicationSettingsUseCase

    init {
        App.instance.dagger.inject(this)
        // устанавливаем сохраненные настройки приложения
        initApplicationSettingsUseCase.execute()
    }

    fun setActivityStatus(isRunning: Boolean) = setActivityRunningStatusUseCase(isRunning)
}


