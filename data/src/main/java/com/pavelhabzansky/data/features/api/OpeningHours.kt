package com.pavelhabzansky.data.features.api

import com.google.gson.annotations.SerializedName

data class OpeningHours(
        @SerializedName("open_now")
        var openNow: Boolean? = null
)