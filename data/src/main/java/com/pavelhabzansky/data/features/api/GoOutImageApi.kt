package com.pavelhabzansky.data.features.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface GoOutImageApi {

    @GET
    fun fetchImage(): Call<ResponseBody>

}