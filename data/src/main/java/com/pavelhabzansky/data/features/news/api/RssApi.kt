package com.pavelhabzansky.data.features.news.api

import retrofit2.Call
import retrofit2.http.GET


interface RssApi {

    @GET("rsshandlerext.aspx?exportId=34&dontparse=false")
    fun fetchNews(): Call<RssFeed>

}