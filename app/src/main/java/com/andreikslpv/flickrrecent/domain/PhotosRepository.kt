package com.andreikslpv.flickrrecent.domain

import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    fun getRecentPhoto(): Flow<Response<PhotoDomainModel>>

    fun getPhotoFromCache(): Flow<Response<PhotoDomainModel>>

    suspend fun addPhotoInFavorites(photo: PhotoDomainModel)

    suspend fun removePhotoFromFavorites(photoId: String)

    fun getFavoritesIds(): List<String>

    fun getFavoritesFlow(): Flow<List<PhotoDomainModel>>

}