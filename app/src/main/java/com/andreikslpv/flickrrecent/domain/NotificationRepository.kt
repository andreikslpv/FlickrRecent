package com.andreikslpv.flickrrecent.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface NotificationRepository {

    fun setActivityRunningStatus(isRunning: Boolean)

    fun getActivityRunningStatus(): Boolean

    fun setIsNeedToUpdatePhoto()

    fun getUpdateStatus(): MutableStateFlow<String>
}