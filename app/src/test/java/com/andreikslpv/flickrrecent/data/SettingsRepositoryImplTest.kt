package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.data.utils.getItem
import com.andreikslpv.flickrrecent.data.utils.observeKey
import com.andreikslpv.flickrrecent.data.utils.putItem
import com.andreikslpv.flickrrecent.domain.models.AppSetting
import com.andreikslpv.flickrrecent.testutils.wellDone
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
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
        settings = SettingsRepositoryImpl(context)
    }

    @Test
    fun `getSettingValue method must return value from preferences`() {
        class SettingTest(key: String = "test", defaultValue: Boolean = false) :
            AppSetting(key, defaultValue)
        every { preferences.getItem(any() as String, any() as Boolean) } returns true

        val result = settings.getSettingValue(SettingTest())

        Assert.assertEquals(true, result)
    }

    @Test
    fun `putSettingValue method must set value in preferences`() {
        mockkStatic("com.andreikslpv.flickrrecent.data.utils.PreferenceExtensionsKt")
        class SettingTest(key: String = "test", defaultValue: Boolean = false) :
            AppSetting(key, defaultValue)
        every { preferences.putItem(SettingTest(), true) } returns Unit

        settings.putSettingValue(SettingTest(), true)

        verify(exactly = 1) {
            preferences.putItem(SettingTest(), true)
        }
        confirmVerified(preferences)
    }

    @Test
    fun `observeSetting method must return flow for value from preferences`() = runTest {
        class SettingTest(key: String = "test", defaultValue: Boolean = false) :
            AppSetting(key, defaultValue)
        every { preferences.observeKey(any() as String, any() as Boolean) } returns flowOf(
            true,
            false
        )

        val job = launch {
            val result =
                settings.observeSetting(SettingTest()).toList()

            coVerifySequence {
                preferences.registerOnSharedPreferenceChangeListener(any())
                preferences.unregisterOnSharedPreferenceChangeListener(any())
            }
            Assert.assertEquals(listOf(true, false), result)
        }
        job.cancel()

        wellDone()
    }

}