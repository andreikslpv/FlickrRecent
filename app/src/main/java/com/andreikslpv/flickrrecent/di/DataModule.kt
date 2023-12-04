package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.data.NotificationRepositoryImpl
import com.andreikslpv.flickrrecent.data.PhotosRepositoryImpl
import com.andreikslpv.flickrrecent.data.SettingsRepositoryImpl
import com.andreikslpv.flickrrecent.domain.NotificationRepository
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(photosRepositoryImpl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository
}