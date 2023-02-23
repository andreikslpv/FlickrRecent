package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import com.andreikslpv.flickrrecent.domain.usecase.RefreshRecentPhotoUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PhotoFragmentViewModel : ViewModel() {

    val photoStateFlow: StateFlow<ApiResult<PhotoDomainModel>> by lazy {
        getRecentPhotoUseCase.execute().asStateFlow()
    }

    @Inject
    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase
    @Inject
    lateinit var refreshRecentPhotoUseCase: RefreshRecentPhotoUseCase

    init {
        App.instance.dagger.inject(this)
        refresh()
    }

    fun refresh() {
        refreshRecentPhotoUseCase.execute()
    }


}