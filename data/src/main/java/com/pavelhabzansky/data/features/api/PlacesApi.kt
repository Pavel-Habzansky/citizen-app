package com.pavelhabzansky.data.features.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("place/nearbysearch/json")
    fun fetchPlaces(
            @Query("location") location: String,
            @Query("radius") radius: String = "5000",
            @Query("type") type: String,
            @Query("key") key: String,
            @Query("pagetoken") pageToken: String = ""
    ): Call<PlacesResponse>

    @GET("place/photo")
    fun fetchImage(
            @Query("maxwidth") maxwidth: Int = 400,
            @Query("maxheight") maxheight: Int = 400,
            @Query("photoreference") photoRef: String,
            @Query("key") key: String
    ): Call<ResponseBody>

}