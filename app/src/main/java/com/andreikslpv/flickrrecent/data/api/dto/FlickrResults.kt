package com.andreikslpv.flickrrecent.data.api.dto

import com.google.gson.annotations.SerializedName

data class FlickrResults(
    @SerializedName("photos")
    val photos: Photos?,
    @SerializedName("stat")
    val stat: String?
)