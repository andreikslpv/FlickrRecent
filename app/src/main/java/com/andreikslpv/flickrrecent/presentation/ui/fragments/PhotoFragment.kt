package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.databinding.FragmentPhotoBinding
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.presentation.ui.utils.makeToast
import com.andreikslpv.flickrrecent.presentation.ui.utils.viewModelCreator
import com.andreikslpv.flickrrecent.presentation.vm.PhotoViewModel
import com.bumptech.glide.Glide
import javax.inject.Inject

class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var factory: PhotoViewModel.Factory
    private val viewModel by viewModelCreator { factory.create() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentPhoto()
        setupSwipeToRefresh()
        initButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeCurrentPhoto() {
        viewModel.currentPhoto.observe(viewLifecycleOwner) { response ->
            when (response) {
                Response.Loading -> {
                    binding.photoProgressBar.show()
                }

                is Response.Success -> {
                    Glide.with(this@PhotoFragment)
                        .load(response.data.linkBigPhoto)
                        .into(binding.photoImage)
                    binding.photoFabFavorites.setImageResource(
                        if (response.data.isFavorite) R.drawable.ic_baseline_favorite
                        else R.drawable.ic_baseline_favorite_border
                    )
                    binding.photoProgressBar.hide()
                }

                is Response.Failure -> {
                    binding.photoProgressBar.hide()
                    getString(R.string.api_error).makeToast(requireContext())
                }
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.photoSwipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.photoSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initButtons() {
        binding.photoFabFavorites.setOnClickListener {
            viewModel.changePhotoStatus()
        }
    }

}