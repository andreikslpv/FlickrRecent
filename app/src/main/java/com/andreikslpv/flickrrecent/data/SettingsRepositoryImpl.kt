package com.andreikslpv.flickrrecent.data

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.models.AppSetting
import com.andreikslpv.flickrrecent.data.utils.getItem
import com.andreikslpv.flickrrecent.data.utils.observeKey
import com.andreikslpv.flickrrecent.data.utils.putItem
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(context: Context) : SettingsRepository {
    private val preference: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun getSettingValue(setting: AppSetting) =
        preference.getItem(setting.key, setting.defaultValue)

    override fun putSettingValue(setting: AppSetting, value: Any) =
        preference.putItem(setting, value)

    override fun observeSetting(setting: AppSetting) =
        preference.observeKey(setting.key, setting.defaultValue)
}