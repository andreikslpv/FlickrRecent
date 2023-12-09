package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.flickrrecent.databinding.FragmentGalleryBinding
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoOnItemClickListener
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoRecyclerAdapter
import com.andreikslpv.flickrrecent.presentation.vm.GalleryViewModel
import com.bumptech.glide.Glide

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var photosAdapter: PhotoRecyclerAdapter

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPhotoListRecycler()
        observeFavoritesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initPhotoListRecycler() {
        binding.galleryRecycler.apply {
            photosAdapter = PhotoRecyclerAdapter(
                object : PhotoOnItemClickListener {
                    override fun click(photo: PhotoDomainModel) {
                        Glide.with(this@GalleryFragment)
                            .load(photo.linkBigPhoto)
                            .centerCrop()
                            .into(binding.galleryImage)
                    }
                },
                object : PhotoOnItemClickListener {
                    override fun click(photo: PhotoDomainModel) {
                        viewModel.removePhotoFromFavorites(photo)
                    }
                }
            )
            adapter = photosAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFavoritesList() {
        viewModel.favorites.observe(viewLifecycleOwner) {
            photosAdapter.changeItems(it)
            photosAdapter.notifyDataSetChanged()
        }
    }

}