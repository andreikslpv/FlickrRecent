package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.RefreshRecentPhotoUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideGetRecentPhotoUseCase(photosRepository: PhotosRepository): GetRecentPhotoUseCase {
        return GetRecentPhotoUseCase(photosRepository)
    }

    @Provides
    @Singleton
    fun provideRefreshRecentPhotoUseCase(photosRepository: PhotosRepository): RefreshRecentPhotoUseCase {
        return RefreshRecentPhotoUseCase(photosRepository)
    }
}