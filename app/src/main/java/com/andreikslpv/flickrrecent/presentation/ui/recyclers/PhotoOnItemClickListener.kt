package com.andreikslpv.flickrrecent.presentation.ui.recyclers

import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

interface PhotoOnItemClickListener {
    fun click(photo: PhotoDomainModel)
}