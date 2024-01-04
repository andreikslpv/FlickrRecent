package com.andreikslpv.flickrrecent.data.db

import io.realm.annotations.Required
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class PhotoRealmModel : RealmObject {

    @PrimaryKey
    var id: String = ""

    @Required
    var linkSmallPhoto: String = ""

    @Required
    var linkBigPhoto: String = ""

    var owner: String = ""
    var title: String = ""

    // необходимо переопределить методы equals() и hashCode() для того,
    // чтобы в тестах корректно производилось сравнение объектов данного класса
    // data class использовать нельзя, т.к. наследуемся от RealmObject

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PhotoRealmModel
        if (id != other.id) return false
        if (linkSmallPhoto != other.linkSmallPhoto) return false
        if (linkBigPhoto != other.linkBigPhoto) return false
        if (owner != other.owner) return false
        if (title != other.title) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + linkSmallPhoto.hashCode()
        result = 31 * result + linkBigPhoto.hashCode()
        result = 31 * result + owner.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}


