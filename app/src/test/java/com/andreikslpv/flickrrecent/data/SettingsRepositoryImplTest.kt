package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import com.andreikslpv.flickrrecent.testutils.arranged
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verifySequence
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsRepositoryImplTest {

    @get:Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var preferences: SharedPreferences

    @RelaxedMockK
    lateinit var editor: SharedPreferences.Editor

    private lateinit var settings: SettingsRepositoryImpl

    @Before
    fun setUp() {
        every { context.getSharedPreferences(any(), any()) } returns preferences
        every { preferences.edit() } returns editor
        every {
            preferences.getBoolean(
                SettingsBooleanType.FIRST_LAUNCH.key,
                SettingsBooleanType.FIRST_LAUNCH.defaultValue
            )
        } returns false

        settings = SettingsRepositoryImpl(context)
    }

    @Test
    fun `getSettingBooleanValue method must return value from preferences`() {
        every { preferences.getBoolean(any(), any()) } returns true

        val result = settings.getSettingBooleanValue(SettingsBooleanType.NOTIFICATION)

        Assert.assertEquals(true, result)
    }

    //TODO
//    @Test
//    fun `getSettingBooleanValueFlow method must return flow for value from preferences`() =
//        runTest {
//            //mockkStatic("com.andreikslpv.flickrrecent.data.SharedPreferencesExtensionsKt")
//            every { preferences.observeKey(any() as String, any() as Boolean) } returns flowOf(
//                true,
//                false
//            )
//
//            val result =
//                settings.getSettingBooleanValueFlow(SettingsBooleanType.NOTIFICATION).toList()
//
//            Assert.assertEquals(listOf(true, false), result)
//        }

    @Test
    fun `setSettingBooleanValue method must set value in preferences`() {
        arranged()

        settings.setSettingBooleanValue(SettingsBooleanType.NOTIFICATION, true)

        verifySequence {
            preferences.getBoolean(any(), any())
            preferences.edit()
            editor.putBoolean(any(), any())
        }
    }

}