package com.andreikslpv.flickrrecent.data

import android.content.Context
import com.andreikslpv.flickrrecent.data.api.FlickrApi
import com.andreikslpv.flickrrecent.data.api.FlickrToDomainMapper
import com.andreikslpv.flickrrecent.data.api.dto.FlickrResults
import com.andreikslpv.flickrrecent.data.cache.PhotoCacheModel
import com.andreikslpv.flickrrecent.data.db.CacheToDomainMapper
import com.andreikslpv.flickrrecent.domain.models.EmptyCacheException
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.UnknownException
import com.andreikslpv.flickrrecent.testutils.arranged
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
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

    @RelaxedMockK
    lateinit var realm: Realm

    private lateinit var photosRepositoryImpl: PhotosRepositoryImpl

    @Before
    fun setUp() {
        photosRepositoryImpl = createPhotosRepositoryImpl(context = context)
    }

    @After
    fun cleanUp() {
        every { realm.close() } just runs
        unmockkAll()
    }

    // getRecentPhoto method --------------------

    @Test
    fun `getRecentPhoto method must call flickrApi_getPhotos() 1 time`() = runTest {
        arranged()

        photosRepositoryImpl.getRecentPhoto().collect()

        coVerify(exactly = 1) { flickrApi.getPhotos() }
        confirmVerified(flickrApi)
    }

    @Test
    fun `getRecentPhoto method must emit Response_Success when response_isSuccessful == true and FlickrToDomainMapper return PhotoDomainModel`() =
        runTest {
            val flickrResult = mockk<FlickrResults>(relaxed = true)
            val responseSuccess = Response.success(200, flickrResult)
            coEvery { flickrApi.getPhotos() } returns responseSuccess
            mockkObject(FlickrToDomainMapper)
            val expectedPhotoDomainModel = mockk<PhotoDomainModel>(relaxed = true)
            every { FlickrToDomainMapper.map(any() as FlickrResults) } returns expectedPhotoDomainModel

            val collectedResults = photosRepositoryImpl.getRecentPhoto().toList()

            assertEquals(2, collectedResults.size)
            assert(collectedResults[0] is com.andreikslpv.flickrrecent.domain.models.Response.Loading)
            assert(collectedResults[1] is com.andreikslpv.flickrrecent.domain.models.Response.Success)
            assertEquals(expectedPhotoDomainModel, collectedResults[1].getValueOrNull())
            // дополнительно проверяем, что при выполнении метода была выполнена запись в бд для кеширования фото
            coVerify(exactly = 1) { realm.write(any()) }
            confirmVerified(realm)
        }

    @Test
    fun `getRecentPhoto method must emit Response_Failure(UnknownException) when response_isSuccessful == true and FlickrToDomainMapper return empty PhotoDomainModel`() =
        runTest {
            val flickrResult = mockk<FlickrResults>(relaxed = true)
            val responseSuccess = Response.success(200, flickrResult)
            coEvery { flickrApi.getPhotos() } returns responseSuccess
            mockkObject(FlickrToDomainMapper)
            every { FlickrToDomainMapper.map(any() as FlickrResults) } returns PhotoDomainModel()

            val collectedResults = photosRepositoryImpl.getRecentPhoto().toList()

            assertEquals(2, collectedResults.size)
            assert(collectedResults[0] is com.andreikslpv.flickrrecent.domain.models.Response.Loading)
            assert(collectedResults[1] is com.andreikslpv.flickrrecent.domain.models.Response.Failure)
            val responseError =
                (collectedResults[1] as com.andreikslpv.flickrrecent.domain.models.Response.Failure).error
            assert(responseError is UnknownException)
        }

    @Test
    fun `getRecentPhoto method must emit Response_Failure when response_isSuccessful == false`() =
        runTest {
            coEvery { flickrApi.getPhotos() } returns Response.error(
                404,
                "ResponseBody".toResponseBody()
            )

            val collectedResults = photosRepositoryImpl.getRecentPhoto().toList()

            assertEquals(2, collectedResults.size)
            assert(collectedResults[0] is com.andreikslpv.flickrrecent.domain.models.Response.Loading)
            assert(collectedResults[1] is com.andreikslpv.flickrrecent.domain.models.Response.Failure)
        }

    // getPhotoFromCache ------------------------

    @Test
    fun `getPhotoFromCache method must call Realm's methods 1 time`() =
        runTest {
            val mockRealmQuery = mockk<RealmQuery<PhotoCacheModel>>(relaxed = true)
            val mockRealmResults = mockk<RealmResults<PhotoCacheModel>>(relaxed = true)
            coEvery { realm.query<PhotoCacheModel>(any(), *anyVararg()) } returns mockRealmQuery
            coEvery { mockRealmQuery.find() } returns mockRealmResults
            coEvery { mockRealmResults.firstOrNull() } returns null

            photosRepositoryImpl.getPhotoFromCache().collect()

            coVerify(exactly = 1) { realm.query<PhotoCacheModel>(any(), *anyVararg()) }
            confirmVerified(realm)
            coVerify(exactly = 1) { mockRealmQuery.find() }
            confirmVerified(mockRealmQuery)
            coVerify(exactly = 1) { mockRealmResults.firstOrNull() }
            confirmVerified(mockRealmResults)
        }

    @Test
    fun `getPhotoFromCache method must emit Response_Success when photo (received from db) != null`() =
        runTest {
            val mockPhotoCacheModel = mockk<PhotoCacheModel>(relaxed = true)
            coEvery {
                realm.query<PhotoCacheModel>(any(), *anyVararg()).find().firstOrNull()
            } returns mockPhotoCacheModel
            mockkObject(CacheToDomainMapper)
            val expectedPhotoDomainModel = mockk<PhotoDomainModel>(relaxed = true)
            every { CacheToDomainMapper.map(any() as PhotoCacheModel) } returns expectedPhotoDomainModel

            val collectedResults = photosRepositoryImpl.getPhotoFromCache().toList()

            assertEquals(2, collectedResults.size)
            assert(collectedResults[0] is com.andreikslpv.flickrrecent.domain.models.Response.Loading)
            assert(collectedResults[1] is com.andreikslpv.flickrrecent.domain.models.Response.Success)
            assertEquals(expectedPhotoDomainModel, collectedResults[1].getValueOrNull())
        }

    @Test
    fun `getPhotoFromCache method must emit Response_Failure(EmptyCacheException) when photo (received from db) == null`() =
        runTest {
            coEvery {
                realm.query<PhotoCacheModel>(any(), *anyVararg()).find().firstOrNull()
            } returns null

            val collectedResults = photosRepositoryImpl.getPhotoFromCache().toList()

            assertEquals(2, collectedResults.size)
            assert(collectedResults[0] is com.andreikslpv.flickrrecent.domain.models.Response.Loading)
            assert(collectedResults[1] is com.andreikslpv.flickrrecent.domain.models.Response.Failure)
            val responseError =
                (collectedResults[1] as com.andreikslpv.flickrrecent.domain.models.Response.Failure).error
            assert(responseError is EmptyCacheException)
        }

    // addPhotoInFavorites ----------------------

    @Test
    fun `addPhotoInFavorites method must call method Realm_write 1 time`() =
        runTest {
            val photoDomainModel = mockk<PhotoDomainModel>(relaxed = true)

            photosRepositoryImpl.addPhotoInFavorites(photoDomainModel)

            coVerify(exactly = 1) { realm.write(any()) }
            confirmVerified(realm)
        }

    // removePhotoFromFavorites -----------------

    @Test
    fun `removePhotoFromFavorites method must call method Realm_write 1 time`() =
        runTest {
            val photoId = "1"

            photosRepositoryImpl.removePhotoFromFavorites(photoId)

            coVerify(exactly = 1) { realm.write(any()) }
            confirmVerified(realm)
        }

    // getFavoritesIds --------------------------

//    @Test
//    fun `getFavoritesIds method must call Realm's methods 1 time`() {
//        val mockRealmQuery = mockk<RealmQuery<PhotoRealmModel>>(relaxed = true)
//        val mockRealmResults = mockk<RealmResults<PhotoRealmModel>>(relaxed = true)
//        every { realm.query<PhotoRealmModel>() } returns mockRealmQuery
//        every { mockRealmQuery.find() } returns mockRealmResults
//
//        mockkStatic("kotlin.reflect.jvm.internal.impl.utils.CollectionsKt")
//        val iterClass = mockkClass(Iterable::class)
//        with(iterClass) {
//            every { mockRealmResults.toList() } returns emptyList()
//        }
//
//
//
//        photosRepositoryImpl.getFavoritesIds()
//
//        verify(exactly = 1) { realm.query<PhotoRealmModel>() }
//        confirmVerified(realm)
//        verify(exactly = 1) { mockRealmQuery.find() }
//        confirmVerified(mockRealmQuery)
//        verify(exactly = 1) { mockRealmResults.toList() }
//        confirmVerified(mockRealmResults)
//    }
//
//    @Test
//    fun `getFavoritesIds method must return list of photo's id`() {
//        val mockPhotoRealmModel1 = PhotoRealmModel().apply {
//            id = "1"
//            owner = ""
//            title = ""
//            linkSmallPhoto = ""
//            linkBigPhoto = ""
//        }
//        val mockPhotoRealmModel2 = PhotoRealmModel().apply {
//            id = "2"
//            owner = ""
//            title = ""
//            linkSmallPhoto = ""
//            linkBigPhoto = ""
//        }
//        every {
//            realm.query<PhotoRealmModel>()
//                .find()
//                .toList()
//        } returns emptyList()
//
//        val results = photosRepositoryImpl.getFavoritesIds()
//
//        assertEquals(emptyList<String>(), results)
//    }


    // ------------------------------------------

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
        realm = mockk<Realm>(relaxed = true)
        val realmConfiguration = mockk<RealmConfiguration>(relaxed = true)
        mockkObject(Realm)
        every { Realm.open(realmConfiguration) } returns realm
        return realm
    }

}