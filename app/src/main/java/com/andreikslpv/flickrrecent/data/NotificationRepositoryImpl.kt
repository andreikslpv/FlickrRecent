package com.andreikslpv.flickrrecent.data

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
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
        isNeedToUpdatePhoto.tryEmit(UUID.randomUUID().toString())
    }

    override fun getUpdateStatus() = isNeedToUpdatePhoto

}