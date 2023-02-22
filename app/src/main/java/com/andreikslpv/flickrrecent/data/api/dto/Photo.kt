package com.andreikslpv.flickrrecent.data.api.dto

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("farm")
    val farm: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isfamily")
    val isFamily: Int?,
    @SerializedName("isfriend")
    val isFriend: Int?,
    @SerializedName("ispublic")
    val isPublic: Int?,
    @SerializedName("owner")
    val owner: String?,
    @SerializedName("secret")
    val secret: String?,
    @SerializedName("server")
    val server: String?,
    @SerializedName("title")
    val title: String?
)