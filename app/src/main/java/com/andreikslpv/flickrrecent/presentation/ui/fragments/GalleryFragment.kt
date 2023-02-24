package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.databinding.FragmentGalleryBinding
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.domain.usecase.RemovePhotoFromFavoritesUseCase
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoOnItemClickListener
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoRecyclerAdapter
import com.andreikslpv.flickrrecent.presentation.vm.GalleryFragmentViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var photosAdapter: PhotoRecyclerAdapter

    @Inject
    lateinit var removePhotoFromFavoritesUseCase: RemovePhotoFromFavoritesUseCase

    private val viewModel: GalleryFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
    }

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
        setCollectors()
    }

    private fun initPhotoListRecycler() {
        binding.galleryRecycler.apply {
            photosAdapter =
                PhotoRecyclerAdapter(
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
                            viewLifecycleOwner.lifecycleScope.launch {
                                removePhotoFromFavoritesUseCase.execute(photo.id)
                            }
                        }
                    }
                )
            adapter = photosAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCollectors() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.photoStateFlow
                        .collect {
                            photosAdapter.changeItems(it)
                            photosAdapter.notifyDataSetChanged()
                        }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}