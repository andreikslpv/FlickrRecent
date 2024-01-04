package com.andreikslpv.flickrrecent.domain.models

sealed class Response<out T> {
    data object Loading : Response<Nothing>()

    data class Success<out T>(
        val data: T
    ) : Response<T>()

    data class Failure(
        val error: Throwable
    ) : Response<Nothing>()

    fun getValueOrNull(): T? {
        if (this is Success<T>) return this.data
        return null
    }

}