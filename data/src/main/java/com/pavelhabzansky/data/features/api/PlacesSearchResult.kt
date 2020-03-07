package com.pavelhabzansky.data.features.api

import com.google.gson.annotations.SerializedName

data class PlacesSearchResult(
        var geometry: Geometry? = null,
        @SerializedName("opening_hours")
        var openingHours: OpeningHours? = null,
        var name: String? = null,
        @SerializedName("place_id")
        var placeId: String? = null,
        var types: List<String> = emptyList(),
        var vicinity: String? = null
)