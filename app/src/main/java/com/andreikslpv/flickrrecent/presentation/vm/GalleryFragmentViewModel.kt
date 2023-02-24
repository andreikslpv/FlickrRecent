package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GalleryFragmentViewModel : ViewModel() {

    val photoStateFlow: StateFlow<List<PhotoDomainModel>> by lazy {
        getFavoritesUseCase.execute()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    }

    @Inject
    lateinit var getFavoritesUseCase: GetFavoritesUseCase

    init {
        App.instance.dagger.inject(this)
    }

}