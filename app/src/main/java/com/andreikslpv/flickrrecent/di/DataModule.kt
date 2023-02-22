package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.data.PhotosRepositoryImpl
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(photosRepositoryImpl: PhotosRepositoryImpl): PhotosRepository
}