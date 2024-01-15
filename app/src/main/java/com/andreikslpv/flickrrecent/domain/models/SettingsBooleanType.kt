package com.andreikslpv.flickrrecent.domain.models

enum class SettingsBooleanType(val key: String, val defaultValue: Boolean) {
    FIRST_LAUNCH("first_launch", true),
    NOTIFICATION("notification", false),
}