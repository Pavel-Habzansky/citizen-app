package com.pavelhabzansky.data.features.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("place/nearbysearch/json")
    fun fetchPlaces(
            @Query("location") location: String,
            @Query("radius") radius: String = "50000",
            @Query("type") type: String,
            @Query("key") key: String
    ): Call<PlacesResponse>

}