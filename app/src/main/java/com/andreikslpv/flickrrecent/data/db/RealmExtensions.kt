package com.andreikslpv.flickrrecent.data.db

import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.BaseRealmObject

/**
 * Wrapper-extension to enable testing toList() extension of the RealmResults class using the MockK
 */
fun <T : BaseRealmObject> RealmResults<T>.toListMock(): List<T> {
    return toList()
}