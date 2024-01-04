package com.andreikslpv.flickrrecent.data.api

import com.andreikslpv.flickrrecent.data.api.dto.Photo
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import org.junit.Assert.assertEquals
import org.junit.Test

class FlickrMapperTest {

    @Test
    fun `DtoToDomainMapper_map() method must map Photo (when some fields is null) to PhotoDomainModel`() {
        val photo = Photo(
            farm = null,
            id = "id",
            isFamily = null,
            isFriend = null,
            isPublic = null,
            owner = null,
            secret = "secret",
            server = "server",
            title = null,
        )

        val expectedPhotoDomainModel = PhotoDomainModel(
            id = "id",
            owner = "",
            title = "",
            linkSmallPhoto = "https://live.staticflickr.com/server/id_secret_q.jpg",
            linkBigPhoto = "https://live.staticflickr.com/server/id_secret_b.jpg",
            isFavorite = false,
        )

        val photoDomainModel = DtoToDomainMapper.map(photo)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `DtoToDomainMapper_map() method must map Photo (when all fields is null) to PhotoDomainModel`() {
        val photo = Photo(
            farm = null,
            id = null,
            isFamily = null,
            isFriend = null,
            isPublic = null,
            owner = null,
            secret = null,
            server = null,
            title = null,
        )

        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = DtoToDomainMapper.map(photo)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

}