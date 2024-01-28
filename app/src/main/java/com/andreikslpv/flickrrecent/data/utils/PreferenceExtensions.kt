package com.andreikslpv.flickrrecent.data.utils

import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.domain.models.AppSetting
import com.andreikslpv.flickrrecent.domain.models.NotValidValueException
import com.andreikslpv.flickrrecent.domain.models.UnsupportedTypeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion

inline fun <reified T> SharedPreferences.observeKey(key: String, default: T): Flow<T> {
    val flow = MutableStateFlow(getItem(key, default))

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
        if (key == k) {
            flow.value = getItem(key, default)!!
        }
    }
    registerOnSharedPreferenceChangeListener(listener)

    return flow
        .onCompletion { unregisterOnSharedPreferenceChangeListener(listener) }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> SharedPreferences.getItem(key: String, default: T): T {
    return when (default) {
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Long -> getLong(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Set<*> -> getStringSet(key, default as Set<String>) as T
        is MutableSet<*> -> getStringSet(key, default as MutableSet<String>) as T
        else -> throw UnsupportedTypeException(type = T::class.java.name)
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> SharedPreferences.putItem(setting: AppSetting, value: T) {
    if (value != null && value!!::class.java.name == setting.defaultValue::class.java.name) {
        val editor = this.edit()
        when (setting.defaultValue) {
            is String -> editor.putString(setting.key, value as String).apply()
            is Int -> editor.putInt(setting.key, value as Int).apply()
            is Long -> editor.putLong(setting.key, value as Long).apply()
            is Boolean -> editor.putBoolean(setting.key, value as Boolean).apply()
            is Float -> editor.putFloat(setting.key, value as Float).apply()
            is Set<*> -> editor.putStringSet(setting.key, value as Set<String>).apply()
            is MutableSet<*> -> editor.putStringSet(setting.key, value as MutableSet<String>)
                .apply()

            else -> throw UnsupportedTypeException(type = T::class.java.name)
        }
    } else throw NotValidValueException()
}