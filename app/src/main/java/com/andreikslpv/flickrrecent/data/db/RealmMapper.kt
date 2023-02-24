package com.andreikslpv.flickrrecent.data.db

import com.andreikslpv.flickrrecent.domain.BaseMapper
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

object RealmToDomainListMapper : BaseMapper<List<PhotoRealmModel>, List<PhotoDomainModel>> {
    override fun map(type: List<PhotoRealmModel>?): List<PhotoDomainModel> {
        return type?.map {
            PhotoDomainModel(
                id = it?.id ?: "",
                owner = it?.owner ?: "",
                title = it?.title ?: "",
                linkSmallPhoto = it?.linkSmallPhoto ?: "",
                linkBigPhoto = it?.linkBigPhoto ?: "",
                isFavorite = true,
            )
        } ?: listOf()
    }
}

object DomainToRealmMapper : BaseMapper<PhotoDomainModel, PhotoRealmModel> {
    override fun map(type: PhotoDomainModel?): PhotoRealmModel {
        return PhotoRealmModel().apply {
            id = type?.id ?: ""
            owner = type?.owner ?: ""
            title = type?.title ?: ""
            linkSmallPhoto = type?.linkSmallPhoto ?: ""
            linkBigPhoto = type?.linkBigPhoto ?: ""
        }
    }
}