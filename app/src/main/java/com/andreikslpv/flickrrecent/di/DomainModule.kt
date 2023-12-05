package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.SettingsRepository
import com.andreikslpv.flickrrecent.domain.usecase.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetPhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.RemovePhotoFromFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.GetNotificationStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetActivityRunningStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.InverseNotificationSettingUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.ObserveNotificationSettingsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

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

    // for settings

    @Provides
    @Singleton
    fun provideInverseNotificationSettingUseCase(
        settingsRepository: SettingsRepository,
    ): InverseNotificationSettingUseCase {
        return InverseNotificationSettingUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideObserveNotificationSettingsUseCase(
        settingsRepository: SettingsRepository,
    ): ObserveNotificationSettingsUseCase {
        return ObserveNotificationSettingsUseCase(settingsRepository)
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