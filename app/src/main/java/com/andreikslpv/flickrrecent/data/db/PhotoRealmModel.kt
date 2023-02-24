package com.andreikslpv.flickrrecent.data.db

import io.realm.annotations.Required
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PhotoRealmModel : RealmObject {

    @PrimaryKey
    var id: String = ""

    @Required
    var linkSmallPhoto: String = ""

    @Required
    var linkBigPhoto: String = ""

    var owner: String = ""
    var title: String = ""
}


