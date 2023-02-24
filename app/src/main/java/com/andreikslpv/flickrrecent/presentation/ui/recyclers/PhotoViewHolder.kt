package com.andreikslpv.flickrrecent.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.flickrrecent.databinding.ItemPhotoBinding
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.bumptech.glide.Glide

class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotoDomainModel) {
        Glide.with(itemView)
            .load(photo.linkSmallPhoto)
            .centerCrop()
            .into(binding.itemImage)
    }
}