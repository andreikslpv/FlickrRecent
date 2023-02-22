package com.andreikslpv.flickrrecent.data.api

import com.andreikslpv.flickrrecent.data.api.dto.Photo
import com.andreikslpv.flickrrecent.domain.BaseMapper
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

object DtoToDomainMapper : BaseMapper<Photo, PhotoDomainModel> {
    override fun map(type: Photo?): PhotoDomainModel {
        return PhotoDomainModel(
            id = type?.id ?: "",
            owner = type?.owner ?: "",
            title = type?.title ?: "",
            linkSmallPhoto = "https://live.staticflickr.com/${type?.server}/${type?.id}_${type?.secret}_q.jpg",
            linkBigPhoto = "https://live.staticflickr.com/${type?.server}/${type?.id}_${type?.secret}_b.jpg",
            isFavorite = false,
        )
    }
}