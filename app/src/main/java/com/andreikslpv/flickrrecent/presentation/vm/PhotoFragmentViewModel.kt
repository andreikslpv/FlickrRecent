package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.usecase.GetPhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.notification.SetIsNeedToUpdatePhotoUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PhotoFragmentViewModel : ViewModel() {

    val currentPhoto: LiveData<Response<PhotoDomainModel>>

    @Inject
    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase

    @Inject
    lateinit var setIsNeedToUpdatePhotoUseCase: SetIsNeedToUpdatePhotoUseCase


    val photoStatusFlow: StateFlow<Boolean> by lazy {
        getPhotoStatusUseCase.execute().asStateFlow()
    }

    @Inject
    lateinit var getPhotoStatusUseCase: GetPhotoStatusUseCase

    init {
        App.instance.dagger.inject(this)
        currentPhoto = getRecentPhotoUseCase().asLiveData()
    }

    fun refresh() = setIsNeedToUpdatePhotoUseCase()

}