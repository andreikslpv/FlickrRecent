package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.AppSetting
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettingValue(setting: AppSetting): Any

    fun putSettingValue(setting: AppSetting, value: Any)

    fun observeSetting(setting: AppSetting): Flow<Any>
}