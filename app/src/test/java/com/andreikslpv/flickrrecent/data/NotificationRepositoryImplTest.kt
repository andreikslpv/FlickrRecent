package com.andreikslpv.flickrrecent.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class NotificationRepositoryImplTest {

    private lateinit var notificationRepositoryImpl: NotificationRepositoryImpl

    @Before
    fun setUp() {
        notificationRepositoryImpl = NotificationRepositoryImpl()
    }

    // setActivityRunningStatus -----------------

    @Test
    fun `setActivityRunningStatus(true) method must set value of isActivityRunning in true`() {
        // Use reflection to access the private variable
        val isActivityRunning: Field =
            NotificationRepositoryImpl::class.java.getDeclaredField("isActivityRunning")
        isActivityRunning.isAccessible = true

        notificationRepositoryImpl.setActivityRunningStatus(true)

        // Get the value of the private variable from the instance
        val isActivityRunningValue: Boolean =
            isActivityRunning.get(notificationRepositoryImpl) as Boolean
        Assert.assertEquals(true, isActivityRunningValue)
    }

    @Test
    fun `setActivityRunningStatus(false) method must set value of isActivityRunning in false`() {
        val isActivityRunning: Field =
            NotificationRepositoryImpl::class.java.getDeclaredField("isActivityRunning")
        isActivityRunning.isAccessible = true

        notificationRepositoryImpl.setActivityRunningStatus(false)

        val isActivityRunningValue: Boolean =
            isActivityRunning.get(notificationRepositoryImpl) as Boolean
        Assert.assertEquals(false, isActivityRunningValue)
    }

    // getActivityRunningStatus -----------------

    @Test
    fun `getActivityRunningStatus() method must return value of isActivityRunning`() {
        val isActivityRunning: Field =
            NotificationRepositoryImpl::class.java.getDeclaredField("isActivityRunning")
        isActivityRunning.isAccessible = true
        val isActivityRunningValue: Boolean =
            isActivityRunning.get(notificationRepositoryImpl) as Boolean

        val result = notificationRepositoryImpl.getActivityRunningStatus()

        Assert.assertEquals(isActivityRunningValue, result)
    }

    // setIsNeedToUpdatePhoto -------------------

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `setIsNeedToUpdatePhoto method must run emit new value for isNeedToUpdatePhoto`() =
        runTest {
            val isNeedToUpdatePhoto: Field =
                NotificationRepositoryImpl::class.java.getDeclaredField("isNeedToUpdatePhoto")
            isNeedToUpdatePhoto.isAccessible = true
            val isNeedToUpdatePhotoValue: MutableStateFlow<String> =
                isNeedToUpdatePhoto.get(notificationRepositoryImpl) as MutableStateFlow<String>
            val startValue = isNeedToUpdatePhotoValue.value

            notificationRepositoryImpl.setIsNeedToUpdatePhoto()

            val newValue = isNeedToUpdatePhotoValue.value
            Assert.assertNotEquals(startValue, newValue)
        }


    // getUpdateStatus --------------------------

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `getUpdateStatus() method must return value of isNeedToUpdatePhoto`() {
        val isNeedToUpdatePhoto: Field =
            NotificationRepositoryImpl::class.java.getDeclaredField("isNeedToUpdatePhoto")
        isNeedToUpdatePhoto.isAccessible = true
        val isNeedToUpdatePhotoValue: MutableStateFlow<String> =
            isNeedToUpdatePhoto.get(notificationRepositoryImpl) as MutableStateFlow<String>

        val result = notificationRepositoryImpl.getUpdateStatus()

        Assert.assertEquals(isNeedToUpdatePhotoValue, result)
    }

}