package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.usecase.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetPhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.InitApplicationSettingsUseCase
import com.andreikslpv.flickrrecent.domain.usecase.InverseBooleanSettingValueUseCase
import com.andreikslpv.flickrrecent.domain.usecase.LoadPhotoFromCacheUseCase
import com.andreikslpv.flickrrecent.domain.usecase.RemovePhotoFromFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.GetNotificationStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideInitApplicationSettingsUseCase(
        settingsRepository: SettingsRepository,
        photosRepository: PhotosRepository
    ): InitApplicationSettingsUseCase {
        return InitApplicationSettingsUseCase(settingsRepository, photosRepository)
    }

    @Provides
    @Singleton
    fun provideInverseBooleanSettingValueUseCase(
        settingsRepository: SettingsRepository,
        photosRepository: PhotosRepository
    ): InverseBooleanSettingValueUseCase {
        return InverseBooleanSettingValueUseCase(settingsRepository, photosRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecentPhotoUseCase(
        photosRepository: PhotosRepository,
        notificationRepository: NotificationRepository,
    ): GetRecentPhotoUseCase {
        return GetRecentPhotoUseCase(photosRepository, notificationRepository)
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

    @Provides
    @Singleton
    fun provideLoadPhotoFromCacheUseCase(photosRepository: PhotosRepository): LoadPhotoFromCacheUseCase {
        return LoadPhotoFromCacheUseCase(photosRepository)
    }

    // for notifications

    @Provides
    @Singleton
    fun provideSetActivityStatusUseCase(notificationRepository: NotificationRepository): SetActivityRunningStatusUseCase {
        return SetActivityRunningStatusUseCase(notificationRepository)
    }

    @Provides
    @Singleton
    fun provideGetNotificationStatusUseCase(
        notificationRepository: NotificationRepository,
        settingsRepository: SettingsRepository,
    ): GetNotificationStatusUseCase {
        return GetNotificationStatusUseCase(notificationRepository, settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSetIsNeedToUpdatePhotoUseCase(notificationRepository: NotificationRepository): SetIsNeedToUpdatePhotoUseCase {
        return SetIsNeedToUpdatePhotoUseCase(notificationRepository)
    }

}