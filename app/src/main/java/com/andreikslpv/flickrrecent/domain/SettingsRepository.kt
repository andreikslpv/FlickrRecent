package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettingBooleanValue(setting: SettingsBooleanType): Boolean

    fun getSettingBooleanValueFlow(setting: SettingsBooleanType): Flow<Boolean>

    fun setSettingBooleanValue(setting: SettingsBooleanType, value: Boolean)
}