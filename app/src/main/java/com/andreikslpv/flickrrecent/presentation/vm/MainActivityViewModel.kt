package com.andreikslpv.flickrrecent.presentation.vm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.usecase.GetNotificationStatusUseCase
import com.andreikslpv.flickrrecent.presentation.ui.utils.AlarmReceiver
import com.andreikslpv.flickrrecent.presentation.ui.utils.cancelNotifications
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

const val TIME_INTERVAL = 15000L

@SuppressLint("UnspecifiedImmutableFlag")
class MainActivityViewModel : ViewModel() {
    private val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notifyPendingIntent: PendingIntent
    private val requestCode = 0
    private val notifyIntent = Intent(App.instance, AlarmReceiver::class.java)
    private var triggerTime: Long = 0L

    var isActivityNotRunning = false

    private val notificationStatusFlow: StateFlow<Boolean> by lazy {
        getNotificationStatusUseCase.execute().asStateFlow()
    }

    @Inject
    lateinit var getNotificationStatusUseCase: GetNotificationStatusUseCase

    init {
        App.instance.dagger.inject(this)

        notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                App.instance,
                requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                App.instance,
                requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                delay(TIME_INTERVAL)
                withContext(Dispatchers.Main) {
                    startTimer()
                }
            }
        }

    }

    private fun startTimer() {
        println("!!! ${notificationStatusFlow.value} $isActivityNotRunning")
        if (notificationStatusFlow.value && isActivityNotRunning) {
            triggerTime = SystemClock.elapsedRealtime() + TIME_INTERVAL

            val notificationManager =
                ContextCompat.getSystemService(
                    App.instance,
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.cancelNotifications()

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                notifyPendingIntent
            )
        }
    }

}


