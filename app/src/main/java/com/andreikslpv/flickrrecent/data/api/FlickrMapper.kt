package com.andreikslpv.flickrrecent.data.api

import com.andreikslpv.flickrrecent.data.api.FlickrConstants.PHOTO_URL
import com.andreikslpv.flickrrecent.data.api.dto.Photo
import com.andreikslpv.flickrrecent.domain.BaseMapper
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

object DtoToDomainMapper : BaseMapper<Photo, PhotoDomainModel> {
    override fun map(type: Photo?): PhotoDomainModel {
        return if (type?.id == null || type.server == null || type.secret == null) PhotoDomainModel()
        else PhotoDomainModel(
            id = type.id,
            owner = type?.owner ?: "",
            title = type?.title ?: "",
            linkSmallPhoto = "$PHOTO_URL/${type.server}/${type.id}_${type.secret}_q.jpg",
            linkBigPhoto = "$PHOTO_URL/${type.server}/${type.id}_${type.secret}_b.jpg",
            isFavorite = false,
        )
    }
}