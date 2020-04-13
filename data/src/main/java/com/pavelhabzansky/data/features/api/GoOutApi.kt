package com.pavelhabzansky.data.features.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoOutApi {

    @GET("activityfeed/v2/feedpublic")
    fun fetchEvents(
            @Query("page") page: Int,
            @Query("locality") locality: Int = 3,
            @Query("language") language: String = "cs"
    ): Call<GoOutResponse>

}