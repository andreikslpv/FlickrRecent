package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.favorites.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.RemovePhotoFromFavoritesUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel @AssistedInject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removePhotoFromFavoritesUseCase: RemovePhotoFromFavoritesUseCase,
) : ViewModel() {

    val favorites = getFavoritesUseCase().asLiveData()

    fun removePhotoFromFavorites(photo: PhotoDomainModel) {
        CoroutineScope(Dispatchers.IO).launch {
            removePhotoFromFavoritesUseCase(photo.id)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): GalleryViewModel
    }
}