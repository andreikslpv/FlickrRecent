package com.andreikslpv.flickrrecent.presentation.ui.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.domain.models.NotificationStatus
import com.andreikslpv.flickrrecent.domain.usecase.notification.GetNotificationStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import javax.inject.Inject


class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getNotificationStatusUseCase: GetNotificationStatusUseCase

    @Inject
    lateinit var setIsNeedToUpdatePhotoUseCase: SetIsNeedToUpdatePhotoUseCase

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)

        when (getNotificationStatusUseCase()) {
            NotificationStatus.NOTIFICATION_ENABLED_AND_SHOWING -> {
                // показываем уведомление в шторке
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.sendNotification(
                    context.getString(R.string.notification_message),
                    context
                )
            }

            NotificationStatus.NOTIFICATION_ENABLED_AND_NOT_SHOWING -> {
                // уведомляем, что надо поменять картинку
                setIsNeedToUpdatePhotoUseCase()
            }

            NotificationStatus.NOTIFICATION_DISABLED -> {}
        }
        // создаем новое событие, которое пройзойдет через заданный промежуток времени
        AlarmUtils.createAlarmEvent(context)
    }

}