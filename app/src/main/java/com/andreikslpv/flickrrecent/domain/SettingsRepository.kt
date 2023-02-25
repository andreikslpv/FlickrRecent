package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType

interface SettingsRepository {

    fun getSettingBooleanValue(setting: SettingsBooleanType): Boolean

    fun setSettingBooleanValue(setting: SettingsBooleanType, value: Boolean)
}