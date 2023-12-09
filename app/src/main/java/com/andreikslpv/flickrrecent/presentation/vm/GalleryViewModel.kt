package com.andreikslpv.flickrrecent.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.favorites.GetFavoritesUseCase
import com.andreikslpv.flickrrecent.domain.usecase.favorites.RemovePhotoFromFavoritesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryViewModel : ViewModel() {

    val favorites: LiveData<List<PhotoDomainModel>>

    @Inject
    lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Inject
    lateinit var removePhotoFromFavoritesUseCase: RemovePhotoFromFavoritesUseCase

    init {
        App.instance.dagger.inject(this)
        favorites = getFavoritesUseCase().asLiveData()
    }

    fun removePhotoFromFavorites(photo: PhotoDomainModel) {
        CoroutineScope(Dispatchers.IO).launch {
            removePhotoFromFavoritesUseCase(photo.id)
        }
    }
}