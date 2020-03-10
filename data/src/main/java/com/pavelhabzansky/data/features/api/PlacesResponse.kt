package com.pavelhabzansky.data.features.api

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
        @SerializedName("next_page_token")
        val nextPageToken: String? = null,
        val results: List<PlacesSearchResult> = emptyList()
)