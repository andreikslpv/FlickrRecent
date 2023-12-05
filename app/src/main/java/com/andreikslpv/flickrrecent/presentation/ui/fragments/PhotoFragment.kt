package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.databinding.FragmentPhotoBinding
import com.andreikslpv.flickrrecent.domain.models.Response
import com.andreikslpv.flickrrecent.domain.usecase.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.presentation.ui.utils.makeToast
import com.andreikslpv.flickrrecent.presentation.vm.PhotoViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var changePhotoStatusUseCase: ChangePhotoStatusUseCase


    private val viewModel: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
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
        observeNotificationSetting()


        setCollectors()
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
                    binding.photoProgressBar.isVisible = true
                }

                is Response.Success -> {
                    Glide.with(this@PhotoFragment)
                        .load(response.data.linkBigPhoto)
                        .fitCenter()
                        .into(binding.photoImage)
                    binding.photoProgressBar.isVisible = false
                }

                is Response.Failure -> {
                    binding.photoProgressBar.isVisible = false
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

    private fun observeNotificationSetting() {
        viewModel.notificationSetting.observe(viewLifecycleOwner) {
            setNotificationIcon(it)
        }
    }

    private fun setNotificationIcon(isEnable: Boolean) {
        binding.photoFabNotification.setImageResource(
            if (isEnable) R.drawable.ic_baseline_notifications
            else R.drawable.ic_baseline_notifications_off
        )
    }



    private fun setCollectors() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.photoStatusFlow
                        .collect {
                            setFavoritesIcon(it)
                        }
                }
            }
        }
    }

    private fun initButtons() {
        binding.photoFabFavorites.setOnClickListener {
//            viewModel.currentPhoto.value.data?.let { photo ->
//                changePhotoStatusUseCase.execute(photo)
//            }
        }

        binding.photoFabNotification.setOnClickListener {
            viewModel.inverseNotificationSetting()
        }
    }

    private fun setFavoritesIcon(isEnable: Boolean) {
        binding.photoFabFavorites.setImageResource(
            if (isEnable) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
    }



}