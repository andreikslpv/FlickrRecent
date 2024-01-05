package com.andreikslpv.flickrrecent.domain.models

open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class UnknownException(message: String = "unknown error") : AppException(message)