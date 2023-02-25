package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface PhotosRepository {

    fun setNotificationStatus(enable: Boolean)

    fun getNotificationStatus(): MutableStateFlow<Boolean>

    fun getRecentPhoto(): MutableStateFlow<ApiResult<PhotoDomainModel>>

    fun refreshRecentPhoto()

    suspend fun addPhotoInFavorites(photo: PhotoDomainModel)

    suspend fun removePhotoFromFavorites(photoId: String)

    fun getPhotoStatus(): MutableStateFlow<Boolean>

    fun isPhotoFavorites(photoId: String): Boolean

    fun getFavorites(): Flow<List<PhotoDomainModel>>

    suspend fun savePhotoToCache()

    suspend fun savePhotoToDisk()

    fun loadPhotoFromCache()

}