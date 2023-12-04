package com.andreikslpv.flickrrecent.data

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.UUID
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    private var isActivityRunning = false
    private val isNeedToUpdatePhoto = MutableStateFlow("")

    override fun setActivityRunningStatus(isRunning: Boolean) {
        isActivityRunning = isRunning
    }

    override fun getActivityRunningStatus() = isActivityRunning

    override fun setIsNeedToUpdatePhoto() {
        CoroutineScope(Dispatchers.IO).launch {
            // Сообщаем, что надо обновить фото с таймаутом, т.к. функция запускается из BroadcastReceiver
            withTimeoutOrNull(TIMEOUT) {
                isNeedToUpdatePhoto.tryEmit(UUID.randomUUID().toString())
            }
        }
    }

    override fun getUpdateStatus() = isNeedToUpdatePhoto

    companion object {
        private const val TIMEOUT = 2000L
    }

}