package com.andreikslpv.flickrrecent.domain.models

data class PhotoDomainModel(
    val id: String = "",
    val owner: String = "",
    val title: String = "",
    var linkSmallPhoto: String = "",
    var linkBigPhoto: String = "",
    var isFavorite: Boolean = false,

    )
