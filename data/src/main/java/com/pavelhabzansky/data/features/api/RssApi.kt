package com.pavelhabzansky.data.features.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface RssApi {

    @GET
    fun fetchNews(@Url url: String): Call<RssFeed>

}