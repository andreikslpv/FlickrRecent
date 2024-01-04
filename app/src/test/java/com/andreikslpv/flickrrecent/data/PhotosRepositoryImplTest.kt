package com.andreikslpv.flickrrecent.data

import android.content.Context
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.api.dto.FlickrResults
import com.andreikslpv.flickrrecent.data.api.dto.Photo
import com.andreikslpv.flickrrecent.data.api.dto.Photos
import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.data.db.PhotoRealmModel
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.testutils.arranged
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

class PhotosRepositoryImplTest {

    @get:Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var flickrApi: FlickrApi

    @MockK
    lateinit var context: Context

    private lateinit var photosRepositoryImpl: PhotosRepositoryImpl

    @Before
    fun setUp() {
        photosRepositoryImpl = createPhotosRepositoryImpl(context = context)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun `getRecentPhoto method must be called 1 time`() = runTest {
        arranged()

        photosRepositoryImpl.getRecentPhoto().collect()

        coVerify(exactly = 1) { flickrApi.getPhotos() }
        confirmVerified(flickrApi)
    }

    @Test
    fun `getRecentPhoto method must return photo from endpoint`() = runTest {
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
        val responseSuccess = Response.success(flickrResult)
        val expectedPhoto = PhotoDomainModel(
            "1",
            "1",
            "1",
            "https://live.staticflickr.com/1/1_1_q.jpg",
            "https://live.staticflickr.com/1/1_1_b.jpg",
            false
        )
        coEvery { flickrApi.getPhotos() } returns responseSuccess

        val collectedResults = photosRepositoryImpl.getRecentPhoto().toList()

        assertEquals(2, collectedResults.size)
        val photo = collectedResults[1]
        assertEquals(expectedPhoto, photo.getValueOrNull())
    }


    private fun createPhotosRepositoryImpl(
        retrofit: Retrofit = createRetrofit(),
        realmDb: Realm = createRealm(),
        context: Context,
    ): PhotosRepositoryImpl {
        return PhotosRepositoryImpl(retrofit, realmDb, context)
    }

    private fun createRetrofit(): Retrofit {
        val retrofit = mockk<Retrofit>()
        every { retrofit.create(FlickrApi::class.java) } returns flickrApi
        return retrofit
    }

    private fun createRealm(): Realm {
        val configuration = RealmConfiguration.create(
            setOf(
                PhotoRealmModel::class,
                PhotoCacheModel::class
            )
        )
        return Realm.open(configuration)
    }

}