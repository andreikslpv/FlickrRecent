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
import com.andreikslpv.flickrrecent.domain.models.ApiStatus
import com.andreikslpv.flickrrecent.domain.models.SettingsBooleanType
import com.andreikslpv.flickrrecent.domain.usecase.ChangePhotoStatusUseCase
import com.andreikslpv.flickrrecent.domain.usecase.InverseBooleanSettingValueUseCase
import com.andreikslpv.flickrrecent.domain.usecase.LoadPhotoFromCacheUseCase
import com.andreikslpv.flickrrecent.presentation.ui.MainActivity
import com.andreikslpv.flickrrecent.presentation.ui.utils.makeToast
import com.andreikslpv.flickrrecent.presentation.vm.PhotoFragmentViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

const val NAME_OF_CACHE = "lastCachedPhoto.png"

class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var changePhotoStatusUseCase: ChangePhotoStatusUseCase

    @Inject
    lateinit var loadPhotoFromCacheUseCase: LoadPhotoFromCacheUseCase

    @Inject
    lateinit var inverseBooleanSettingValueUseCase: InverseBooleanSettingValueUseCase

    private val viewModel: PhotoFragmentViewModel by viewModels()

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

        setCollectors()
        setupSwipeToRefresh()
        initButtons()
    }

    private fun setCollectors() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.photoStateFlow.collect { result ->
                        when (result.status) {
                            ApiStatus.SUCCESS -> {
                                Glide.with(this@PhotoFragment)
                                    .load(result.data?.linkBigPhoto)
                                    .fitCenter()
                                    .into(binding.photoImage)
                                binding.photoProgressBar.isVisible = false
                            }
                            ApiStatus.ERROR -> {
                                binding.photoProgressBar.isVisible = false
                                getString(R.string.api_error).makeToast(requireContext())
                                loadPhotoFromCacheUseCase.execute()
                            }
                            ApiStatus.LOADING -> {
                                binding.photoProgressBar.isVisible = true
                            }
                            ApiStatus.CACHE -> {
                                Glide.with(this@PhotoFragment)
                                    .load("${requireContext().filesDir}${File.separator}$NAME_OF_CACHE")
                                    .fitCenter()
                                    .into(binding.photoImage)
                                binding.photoProgressBar.isVisible = false
                                getString(R.string.load_from_cache).makeToast(requireContext())
                            }
                        }
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.photoStatusFlow
                        .collect {
                            setFavoritesIcon(it)
                        }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.notificationStatusFlow
                        .collect {
                            setNotificationIcon(it)
                        }
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
            viewModel.photoStateFlow.value.data?.let { photo ->
                changePhotoStatusUseCase.execute(photo)
            }
        }

        binding.photoFabNotification.setOnClickListener {
            inverseBooleanSettingValueUseCase.execute(SettingsBooleanType.NOTIFICATION)
        }
    }

    private fun setFavoritesIcon(isEnable: Boolean) {
        binding.photoFabFavorites.setImageResource(
            if (isEnable) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
    }

    private fun setNotificationIcon(isEnable: Boolean) {
        binding.photoFabNotification.setImageResource(
            if (isEnable) R.drawable.ic_baseline_notifications
            else R.drawable.ic_baseline_notifications_off
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}