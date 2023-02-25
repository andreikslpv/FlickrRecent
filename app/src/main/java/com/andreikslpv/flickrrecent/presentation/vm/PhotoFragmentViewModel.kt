package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.GetNotificationStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetPhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.RefreshRecentPhotoUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PhotoFragmentViewModel : ViewModel() {

    val photoStateFlow: StateFlow<ApiResult<PhotoDomainModel>> by lazy {
        getRecentPhotoUseCase.execute().asStateFlow()
    }
    val photoStatusFlow: StateFlow<Boolean> by lazy {
        getPhotoStatusUseCase.execute().asStateFlow()
    }
    val notificationStatusFlow: StateFlow<Boolean> by lazy {
        getNotificationStatusUseCase.execute().asStateFlow()
    }

    @Inject
    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase

    @Inject
    lateinit var refreshRecentPhotoUseCase: RefreshRecentPhotoUseCase

    @Inject
    lateinit var getPhotoStatusUseCase: GetPhotoStatusUseCase

    @Inject
    lateinit var getNotificationStatusUseCase: GetNotificationStatusUseCase

    init {
        App.instance.dagger.inject(this)
        refresh()
    }

    fun refresh() {
        refreshRecentPhotoUseCase.execute()
    }


}