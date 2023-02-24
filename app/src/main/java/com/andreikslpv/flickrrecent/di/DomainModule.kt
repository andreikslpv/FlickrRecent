package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.usecase.*
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

    @Provides
    @Singleton
    fun provideRemovePhotoFromFavoritesUseCase(photosRepository: PhotosRepository): RemovePhotoFromFavoritesUseCase {
        return RemovePhotoFromFavoritesUseCase(photosRepository)
    }

    @Provides
    @Singleton
    fun provideChangePhotoStatusUseCase(photosRepository: PhotosRepository): ChangePhotoStatusUseCase {
        return ChangePhotoStatusUseCase(photosRepository)
    }

    @Provides
    @Singleton
    fun provideGetPhotoStatusUseCase(photosRepository: PhotosRepository): GetPhotoStatusUseCase {
        return GetPhotoStatusUseCase(photosRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(photosRepository: PhotosRepository): GetFavoritesUseCase {
        return GetFavoritesUseCase(photosRepository)
    }
}