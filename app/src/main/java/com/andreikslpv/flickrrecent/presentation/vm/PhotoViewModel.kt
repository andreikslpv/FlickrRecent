package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.InverseNotificationSettingUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.ObserveNotificationSettingsUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine

class PhotoViewModel @AssistedInject constructor(
    getRecentPhotoUseCase: GetRecentPhotoUseCase,
    private val setIsNeedToUpdatePhotoUseCase: SetIsNeedToUpdatePhotoUseCase,
    observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase,
    private val inverseNotificationSettingUseCase: InverseNotificationSettingUseCase,
    private val changePhotoStatusUseCase: ChangePhotoStatusUseCase,
    getFavoritesUseCase: GetFavoritesUseCase,
) : ViewModel() {

    val currentPhoto = combine(
        getRecentPhotoUseCase(),
        getFavoritesUseCase(),
        ::merge,
    ).asLiveData()

    val notificationSetting = observeNotificationSettingsUseCase().asLiveData()

    private fun merge(
        recentPhoto: Response<PhotoDomainModel>,
        favorites: List<PhotoDomainModel>,
    ): Response<PhotoDomainModel> {
        return when (recentPhoto) {
            Response.Loading, is Response.Failure -> recentPhoto
            is Response.Success -> {
                val favoriteIds = favorites.map { it.id }
                val temp = recentPhoto.data
                temp.isFavorite = favoriteIds.contains(temp.id)
                Response.Success(temp)
            }
        }
    }

    fun refresh() = setIsNeedToUpdatePhotoUseCase()

    fun inverseNotificationSetting() = inverseNotificationSettingUseCase()

    fun changePhotoStatus() {
        if (currentPhoto.value is Response.Success)
            changePhotoStatusUseCase((currentPhoto.value as Response.Success<PhotoDomainModel>).data)
    }

    @AssistedFactory
    interface Factory {
        fun create(): PhotoViewModel
    }

}