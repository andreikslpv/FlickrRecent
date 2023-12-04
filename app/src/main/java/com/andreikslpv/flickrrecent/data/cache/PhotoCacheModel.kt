package com.andreikslpv.flickrrecent.data.cache

import io.realm.annotations.Required
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PhotoCacheModel : RealmObject {

    @PrimaryKey
    var id: String = "1"

    @Required
    var photoId: String = ""

    @Required
    var linkSmallPhoto: String = ""

    @Required
    var linkBigPhoto: String = ""

    var owner: String = ""
    var title: String = ""
}