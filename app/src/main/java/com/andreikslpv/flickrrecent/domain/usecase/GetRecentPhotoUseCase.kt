package com.andreikslpv.flickrrecent.domain.usecase

import com.andreikslpv.flickrrecent.domain.NotificationRepository
import com.andreikslpv.flickrrecent.domain.PhotosRepository
import com.andreikslpv.flickrrecent.domain.models.Response
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetRecentPhotoUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val notificationRepository: NotificationRepository,
) {
    operator fun invoke() = channelFlow {
        send(Response.Loading)
        notificationRepository.getUpdateStatus().collect {
            photosRepository.getRecentPhoto().collectLatest { response ->
                send(response)
                if (response is Response.Failure)
                    photosRepository.getPhotoFromCache().collectLatest { send(it) }
            }
        }
    }
}