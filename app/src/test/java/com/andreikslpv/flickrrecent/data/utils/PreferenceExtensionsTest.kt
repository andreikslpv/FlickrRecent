package com.andreikslpv.flickrrecent.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.domain.models.AppException
import com.andreikslpv.flickrrecent.domain.models.AppSetting
import com.andreikslpv.flickrrecent.domain.models.NotValidValueException
import com.andreikslpv.flickrrecent.domain.models.UnsupportedTypeException
import com.andreikslpv.flickrrecent.testutils.arranged
import com.andreikslpv.flickrrecent.testutils.catch
import com.andreikslpv.flickrrecent.testutils.wellDone
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verifySequence
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreferenceExtensionsTest {

    @get:Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var preferences: SharedPreferences

    @RelaxedMockK
    lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        every { context.getSharedPreferences(any(), any()) } returns preferences
        every { preferences.edit() } returns editor
    }

    // observeKey -------------------------------

    @Test
    fun `observeKey method must return flow for value from preferences`() = runTest {
        arranged()

        val job = launch {
            preferences.observeKey("test", false).toList()

            coVerifySequence {
                preferences.registerOnSharedPreferenceChangeListener(any())
                preferences.unregisterOnSharedPreferenceChangeListener(any())
            }
        }
        job.cancel()

        wellDone()
    }

    // getItem ----------------------------------

    @Test
    fun `getItem method must return value (String) from preferences when param default is String`() {
        every { preferences.getString(any(), any()) } returns "test"

        val result = preferences.getItem("test", "default")

        Assert.assertEquals("test", result)
    }

    @Test
    fun `getItem method must return value (Int) from preferences when param default is Int`() {
        every { preferences.getInt(any(), any()) } returns 1

        val result = preferences.getItem("test", 0)

        Assert.assertEquals(1, result)
    }

    @Test
    fun `getItem method must return value (Long) from preferences when param default is Long`() {
        every { preferences.getLong(any(), any()) } returns 1L

        val result = preferences.getItem("test", 0L)

        Assert.assertEquals(1L, result)
    }

    @Test
    fun `getItem method must return value (Boolean) from preferences when param default is Boolean`() {
        every { preferences.getBoolean(any(), any()) } returns true

        val result = preferences.getItem("test", false)

        Assert.assertEquals(true, result)
    }

    @Test
    fun `getItem method must return value (Float) from preferences when param default is Float`() {
        every { preferences.getFloat(any(), any()) } returns 1f

        val result = preferences.getItem("test", 0f)

        Assert.assertEquals(1f, result)
    }

    @Test
    fun `getItem method must return value (Set_String) from preferences when param default is Set_String`() {
        every { preferences.getStringSet(any(), any()) } returns setOf("1", "2")

        val result = preferences.getItem("test", setOf("0", "3"))

        Assert.assertEquals(setOf("1", "2"), result)
    }

    @Test
    fun `getItem method must return value (MutableSet_String) from preferences when param default is MutableSet_String`() {
        every { preferences.getStringSet(any(), any()) } returns mutableSetOf("1", "2")

        val result = preferences.getItem("test", mutableSetOf("0", "3"))

        Assert.assertEquals(mutableSetOf("1", "2"), result)
    }

    @Test
    fun `getItem method must throw UnsupportedTypeException when type of param default is not supported`() {
        arranged()

        val exception: AppException = catch {
            preferences.getItem("test", Unit)
        }

        Assert.assertEquals(
            UnsupportedTypeException(type = Unit::class.java.name).message,
            exception.message
        )
    }

    // putItem ----------------------------------

    @Test
    fun `putItem method must set value (String) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: String = "") :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), "test")

        verifySequence {
            preferences.edit()
            editor.putString(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (Int) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: Int = 0) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), 1)

        verifySequence {
            preferences.edit()
            editor.putInt(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (Long) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: Long = 0L) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), 1L)

        verifySequence {
            preferences.edit()
            editor.putLong(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (Boolean) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: Boolean = false) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), true)

        verifySequence {
            preferences.edit()
            editor.putBoolean(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (Float) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: Float = 0f) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), 1f)

        verifySequence {
            preferences.edit()
            editor.putFloat(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (Set_String) in preferences`() {
        class SettingTest(key: String = "test", defaultValue: Set<String> = setOf("1", "2")) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), setOf("0", "3"))

        verifySequence {
            preferences.edit()
            editor.putStringSet(any(), any())
        }
    }

    @Test
    fun `putItem method must set value (MutableSet_String) in preferences`() {
        class SettingTest(
            key: String = "test",
            defaultValue: MutableSet<String> = mutableSetOf("1", "2")
        ) :
            AppSetting(key, defaultValue)

        preferences.putItem(SettingTest(), mutableSetOf("0", "3"))

        verifySequence {
            preferences.edit()
            editor.putStringSet(any(), any())
        }
    }

    @Test
    fun `putItem method must throw UnsupportedTypeException when type of param default is not supported`() {
        class SettingTest(key: String = "test", defaultValue: Double = 1.1) :
            AppSetting(key, defaultValue)

        val exception: AppException = catch {
            preferences.putItem(SettingTest(), 1.2)
        }

        Assert.assertEquals(
            UnsupportedTypeException(type = java.lang.Double::class.java.name).message,
            exception.message
        )
    }

    @Test
    fun `putItem method must throw NotValidValueException when type of param default is not match with type of value`() {
        class SettingTest(key: String = "test", defaultValue: Boolean = false) :
            AppSetting(key, defaultValue)

        val exception: AppException = catch {
            preferences.putItem(SettingTest(), "test")
        }

        Assert.assertEquals(
            NotValidValueException().message,
            exception.message
        )
    }

}