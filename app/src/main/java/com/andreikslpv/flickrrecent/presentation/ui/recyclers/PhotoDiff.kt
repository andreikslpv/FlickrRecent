package com.andreikslpv.flickrrecent.presentation.ui.recyclers

import androidx.recyclerview.widget.DiffUtil
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

class PhotoDiff(
    private val oldList: List<PhotoDomainModel>,
    private val newList: List<PhotoDomainModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}