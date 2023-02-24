package com.andreikslpv.flickrrecent.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.flickrrecent.databinding.ItemPhotoBinding
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel

class PhotoRecyclerAdapter(
    private val imageClickListener: PhotoOnItemClickListener,
    private val fabClickListener: PhotoOnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<PhotoDomainModel>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(items[position])
                holder.binding.itemContainer.setOnClickListener {
                    imageClickListener.click(items[position])
                }
                holder.binding.itemFabDelete.setOnClickListener {
                    fabClickListener.click(items[position])
                }
            }
        }
    }

    fun changeItems(list: List<PhotoDomainModel>) {
        val diff = PhotoDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}