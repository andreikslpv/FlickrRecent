package com.andreikslpv.flickrrecent.data.api

import com.andreikslpv.flickrrecent.data.api.dto.FlickrResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlickrApi {
    @GET("{path_1}/{path_2}/")
    suspend fun getPhotos(
        @Path("path_1") path1: String = FlickrConstants.PATH_1,
        @Path("path_2") path2: String = FlickrConstants.PATH_2,
        @Query("method") method: String = FlickrConstants.METHOD,
        @Query("api_key") apiKey: String = FlickrKey.KEY,
        @Query("per_page") perPage: Int = FlickrConstants.PER_PAGE,
        @Query("format") format: String = FlickrConstants.FORMAT,
        @Query("nojsoncallback") noJsonCallback: String = FlickrConstants.NO_JSON_CALLBACK,
    ): Response<FlickrResults>
}