package com.andreikslpv.flickrrecent.domain.models

open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class UnknownException(message: String = "Unknown error") : AppException(message)

class EmptyCacheException(message: String = "Cache is empty") : AppException(message)

class NotValidValueException(message: String = "value is null or type of value don't match type of AppSetting") : AppException(message)

class UnsupportedTypeException(message: String = "Generic type not handle", type: String): AppException("$message $type")