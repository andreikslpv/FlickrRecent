package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(context: Context) : SettingsRepository {
    private val preference: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    init {
        //Логика для первого запуска приложения, чтобы положить дефолтные настройки,
        if (preference.getBoolean(
                SettingsBooleanType.FIRST_LAUNCH.key,
                SettingsBooleanType.FIRST_LAUNCH.defaultValue
            )
        ) {
            preference.edit()
                .putBoolean(
                    SettingsBooleanType.NOTIFICATION.key,
                    SettingsBooleanType.NOTIFICATION.defaultValue
                )
                .apply()
            preference.edit().putBoolean(SettingsBooleanType.FIRST_LAUNCH.key, false).apply()
        }
    }

    override fun getSettingBooleanValue(setting: SettingsBooleanType): Boolean {
        return preference.getBoolean(setting.key, setting.defaultValue)
    }

    override fun setSettingBooleanValue(setting: SettingsBooleanType, value: Boolean) {
        preference.edit().putBoolean(setting.key, value).apply()
    }
}