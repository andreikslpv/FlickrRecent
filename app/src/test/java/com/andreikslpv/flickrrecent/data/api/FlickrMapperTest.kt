package com.andreikslpv.flickrrecent.data.api

import com.andreikslpv.flickrrecent.data.api.dto.FlickrResults
import com.andreikslpv.flickrrecent.data.api.dto.Photo
import com.andreikslpv.flickrrecent.data.api.dto.Photos
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FlickrMapperTest {

    @get:Rule
    val rule = MockKRule(this)

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
    fun `DtoToDomainMapper_map() method must map Photo (when all fields is null) to empty PhotoDomainModel`() {
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

    // FlickrToDomainMapper

    @Test
    fun `FlickrToDomainMapper_map() method must map FlickrResults (when FlickrResults is null) to empty PhotoDomainModel`() {
        val flickrResult: FlickrResults? = null
        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `FlickrToDomainMapper_map() method must map FlickrResults (when FlickrResults_photos is null) to empty PhotoDomainModel`() {
        val flickrResult = FlickrResults(null, null)
        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `FlickrToDomainMapper_map() method must map FlickrResults (when FlickrResults_photos_photo is null) to empty PhotoDomainModel`() {
        val flickrResult = FlickrResults(
            Photos(
                1,
                1,
                1,
                null,
                1
            ),
            null
        )
        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `FlickrToDomainMapper_map() method must map FlickrResults (when FlickrResults_photos_photo is empty) to empty PhotoDomainModel`() {
        val flickrResult = FlickrResults(
            Photos(
                1,
                1,
                1,
                listOf(),
                1
            ),
            null
        )
        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `FlickrToDomainMapper_map() method must map FlickrResults (when FlickrResults_photos_photo_0 is null) to empty PhotoDomainModel`() {
        val flickrResult = FlickrResults(
            Photos(
                1,
                1,
                1,
                listOf(null),
                1
            ),
            null
        )
        val expectedPhotoDomainModel = PhotoDomainModel()

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

    @Test
    fun `FlickrToDomainMapper_map() method must return PhotoDomainModel received from DtoToDomainMapper`() {
        mockkObject(DtoToDomainMapper)
        val expectedPhotoDomainModel = mockk<PhotoDomainModel>(relaxed = true)
        every { DtoToDomainMapper.map(any() as Photo) } returns expectedPhotoDomainModel
        val flickrResult = FlickrResults(
            Photos(
                1,
                1,
                1,
                listOf(
                    Photo(1, "1", 1, 1, 1, "1", "1", "1", "1")
                ),
                1
            ),
            null
        )

        val photoDomainModel = FlickrToDomainMapper.map(flickrResult)

        assertEquals(expectedPhotoDomainModel, photoDomainModel)
    }

}