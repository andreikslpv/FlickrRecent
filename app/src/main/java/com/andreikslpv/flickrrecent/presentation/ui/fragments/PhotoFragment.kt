package com.andreikslpv.flickrrecent.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andreikslpv.flickrrecent.databinding.FragmentPhotoBinding
import com.andreikslpv.flickrrecent.domain.models.ApiResult
import com.andreikslpv.flickrrecent.domain.models.ApiStatus
import com.andreikslpv.flickrrecent.presentation.vm.PhotoFragmentViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: PhotoFragmentViewModel by viewModels()

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
    }

    private fun setCollectors() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.photoStateFlow
                        .collect {result ->
                            when(result.status) {
                                ApiStatus.SUCCESS ->  {
                                    println("I/o success ${result.data}")
                                    Glide.with(this@PhotoFragment)
                                        .load(result.data?.linkBigPhoto)
                                        .fitCenter()
                                        .into(binding.photoImage)
                                    binding.photoProgressBar.isVisible = false
                                }
                                ApiStatus.ERROR ->   {
                                    println("I/o error ${result.message}")
                                    binding.photoProgressBar.isVisible = false
                                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                                }
                                ApiStatus.LOADING ->  {
                                    println("I/o loading")
                                    binding.photoProgressBar.isVisible = true
                                }
                            }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}