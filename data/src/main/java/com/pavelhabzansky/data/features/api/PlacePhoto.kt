package com.pavelhabzansky.data.features.api

import com.google.gson.annotations.SerializedName

data class PlacePhoto(
        var height: Int? = null,
        var width: Int? = null,
        @SerializedName("photo_reference")
        var photoRef: String? = null
)