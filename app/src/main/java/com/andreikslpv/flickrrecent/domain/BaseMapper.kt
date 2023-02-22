package com.andreikslpv.flickrrecent.domain

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}