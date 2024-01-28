package com.andreikslpv.flickrrecent.domain.models

/**
 * The parameter [defaultValue] can only be of the following types:
 * String, Int, Long, Boolean, Float, Set<String>, MutableSet<String>
 */
open class AppSetting(val key: String, val defaultValue: Any)

class SettingNotification(key: String = "notification", defaultValue: Boolean = false) :
    AppSetting(key, defaultValue)