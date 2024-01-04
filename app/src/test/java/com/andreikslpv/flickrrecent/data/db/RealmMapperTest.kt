package com.andreikslpv.flickrrecent.data.db

import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import org.junit.Assert.assertEquals
import org.junit.Test

class RealmMapperTest {

    @Test
    fun `RealmToDomainListMapper_map() method must map list of PhotoRealmModel to list of PhotoDomainModel`() {
        val listOfPhotoRealmModel = listOf(
            PhotoRealmModel().apply {
                id = "1"
                owner = ""
                title = ""
                linkSmallPhoto = ""
                linkBigPhoto = ""
            },
            PhotoRealmModel().apply {
                id = "2"
                owner = ""
                title = ""
                linkSmallPhoto = ""
                linkBigPhoto = ""
            }
        )
        val expectedListOfPhotoDomainModel = listOf(
            PhotoDomainModel(
                id = "1",
                owner = "",
                title = "",
                linkSmallPhoto = "",
                linkBigPhoto = "",
                isFavorite = true,
            ),
            PhotoDomainModel(
                id = "2",
                owner = "",
                title = "",
                linkSmallPhoto = "",
                linkBigPhoto = "",
                isFavorite = true,
            )
        )

        val listOfPhotoDomainModel = RealmToDomainListMapper.map(listOfPhotoRealmModel)

        assertEquals(expectedListOfPhotoDomainModel, listOfPhotoDomainModel)
    }

    @Test
    fun `CacheToDomainMapper_map() method must map PhotoCacheModel to PhotoDomainModel`() {
        val photoCacheModel = PhotoCacheModel().apply {
            id = "1"
            photoId = "2"
            owner = ""
            title = ""
            linkSmallPhoto = ""
            linkBigPhoto = ""
        }
        val expectedPhotoDomainModel = PhotoDomainModel(
            id = "2",
            owner = "",
            title = "",
            linkSmallPhoto = "",
            linkBigPhoto = "",
            isFavorite = false,
        )

        val photoDomainModel = CacheToDomainMapper.map(photoCacheModel)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `DomainToRealmMapper_map() method must map PhotoDomainModel to PhotoRealmModel`() {
        val photoDomainModel = PhotoDomainModel(
            id = "2",
            owner = "",
            title = "",
            linkSmallPhoto = "",
            linkBigPhoto = "",
            isFavorite = false,
        )
        val expectedPhotoRealmModel = PhotoRealmModel().apply {
            id = "2"
            owner = ""
            title = ""
            linkSmallPhoto = ""
            linkBigPhoto = ""
        }

        val photoRealmModel = DomainToRealmMapper.map(photoDomainModel)

        assertEquals(expectedPhotoRealmModel, photoRealmModel)
    }


    @Test
    fun `DomainToCacheMapper_map() method must map PhotoDomainModel to PhotoCacheModel`() {
        val photoDomainModel = PhotoDomainModel(
            id = "2",
            owner = "",
            title = "",
            linkSmallPhoto = "",
            linkBigPhoto = "",
            isFavorite = false,
        )
        val expectedPhotoCacheModel = PhotoCacheModel().apply {
            id = "1"
            photoId = "2"
            owner = ""
            title = ""
            linkSmallPhoto = ""
            linkBigPhoto = ""
        }

        val photoCacheModel = DomainToCacheMapper.map(photoDomainModel)

        assertEquals(expectedPhotoCacheModel, photoCacheModel)
    }

}