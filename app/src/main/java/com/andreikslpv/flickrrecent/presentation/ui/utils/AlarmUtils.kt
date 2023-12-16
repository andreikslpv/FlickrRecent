package com.andreikslpv.flickrrecent.presentation.ui.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat

const val TIME_INTERVAL = 15000L

object AlarmUtils {

    @SuppressLint("ScheduleExactAlarm")
    fun createAlarmEvent(context: Context) {
        val requestCode = 0
        val notifyIntent =
            Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val triggerTime = SystemClock.elapsedRealtime() + TIME_INTERVAL

        //Получаем доступ к AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Устанавливаем Alarm
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            notifyPendingIntent
        )
    }
}