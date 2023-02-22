package com.andreikslpv.flickrrecent.domain.models

data class PhotoDomainModel(
    val id: String = "",
    val owner: String = "",
    val title: String = "",
    val linkSmallPhoto: String = "",
    val linkBigPhoto: String = "",
    var isFavorite: Boolean = false,

    /*val secret: String  = "",
    val server: String  = "",
    val farm: Int = 0,
    val isFamily: Boolean = false,
    val isFriend: Boolean = false,
    val isPublic: Boolean = false,*/

)
