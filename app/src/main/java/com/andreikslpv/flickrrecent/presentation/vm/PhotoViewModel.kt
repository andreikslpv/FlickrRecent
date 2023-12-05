package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.usecase.GetPhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.InverseNotificationSettingUseCase
import com.andreikslpv.flickrrecent.domain.usecase.settings.ObserveNotificationSettingsUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // TODO

    val photoStatusFlow: StateFlow<Boolean> by lazy {
        getPhotoStatusUseCase.execute().asStateFlow()
    }

    @Inject
    lateinit var getPhotoStatusUseCase: GetPhotoStatusUseCase

    init {
        App.instance.dagger.inject(this)
        currentPhoto = getRecentPhotoUseCase().asLiveData()
        notificationSetting = observeNotificationSettingsUseCase().asLiveData()
    }

    fun refresh() = setIsNeedToUpdatePhotoUseCase()

    fun inverseNotificationSetting() = inverseNotificationSettingUseCase()

}