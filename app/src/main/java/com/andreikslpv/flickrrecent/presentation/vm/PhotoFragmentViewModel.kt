package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.GetRecentPhotoUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PhotoFragmentViewModel : ViewModel() {
    var photoStateFlow: StateFlow<ApiResult<PhotoDomainModel>>

    @Inject
    lateinit var getRecentPhotoUseCase: GetRecentPhotoUseCase

    init {
        App.instance.dagger.inject(this)

        photoStateFlow = getRecentPhotoUseCase.execute().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ApiResult.Loading(null, true)
        )
    }


}