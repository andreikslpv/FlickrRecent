package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.InverseNotificationSettingUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.ObserveNotificationSettingsUseCase
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PhotoViewModel : ViewModel() {

    val currentPhoto: LiveData<Response<PhotoDomainModel>>
    val notificationSetting: LiveData<Boolean>

    @Inject
    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase

    @Inject
    lateinit var setIsNeedToUpdatePhotoUseCase: SetIsNeedToUpdatePhotoUseCase

    @Inject
    lateinit var observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase

    @Inject
    lateinit var inverseNotificationSettingUseCase: InverseNotificationSettingUseCase

    @Inject
    lateinit var changePhotoStatusUseCase: ChangePhotoStatusUseCase

    @Inject
    lateinit var getFavoritesUseCase: GetFavoritesUseCase

    init {
        App.instance.dagger.inject(this)
        currentPhoto = combine(
            getRecentPhotoUseCase(),
            getFavoritesUseCase(),
            ::merge,
        ).asLiveData()
        notificationSetting = observeNotificationSettingsUseCase().asLiveData()
    }

    private fun merge(
        recentPhoto: Response<PhotoDomainModel>,
        favorites: List<PhotoDomainModel>,
    ): Response<PhotoDomainModel> {
        return when (recentPhoto) {
            Response.Loading, is Response.Failure -> recentPhoto
            is Response.Success -> {
                val favoriteIds = favorites.map { it.id }
                println("AAAA merge = $favoriteIds")
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

}