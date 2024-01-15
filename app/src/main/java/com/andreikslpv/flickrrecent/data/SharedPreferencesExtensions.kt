package com.andreikslpv.flickrrecent.data

import android.content.SharedPreferences
import com.andreikslpv.flickrrecent.presentation.ui.utils.observeKey
import kotlinx.coroutines.flow.Flow

/**
 * Wrapper-extension to enable testing observeKeyMock() extension of the SharedPreferences class using the MockK
 */
inline fun <reified T> SharedPreferences.observeKeyMock(key: String, default: T): Flow<T> {
    return this.observeKey(key, default)
}