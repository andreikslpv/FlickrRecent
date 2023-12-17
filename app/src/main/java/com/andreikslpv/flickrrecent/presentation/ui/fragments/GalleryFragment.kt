package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.databinding.FragmentGalleryBinding
import com.andreikslpv.flickrrecent.domain.models.PhotoDomainModel
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoOnItemClickListener
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.PhotoRecyclerAdapter
import com.andreikslpv.flickrrecent.presentation.ui.recyclers.SpaceItemDecoration
import com.andreikslpv.flickrrecent.presentation.ui.utils.viewModelCreator
import com.andreikslpv.flickrrecent.presentation.vm.GalleryViewModel
import com.bumptech.glide.Glide
import javax.inject.Inject

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var photosAdapter: PhotoRecyclerAdapter

    @Inject
    lateinit var factory: GalleryViewModel.Factory
    private val viewModel by viewModelCreator { factory.create() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
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
        observeFavoritesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initPhotoListRecycler() {
        val decorator = SpaceItemDecoration(
            paddingTopInDp = 4,
            paddingBottomInDp = 4,
            paddingRightInDp = 4,
            paddingLeftInDp = 8,
        )
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
            addItemDecoration(decorator)
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