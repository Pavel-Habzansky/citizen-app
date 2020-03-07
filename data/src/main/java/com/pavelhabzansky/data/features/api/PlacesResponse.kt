package com.pavelhabzansky.data.features.api

data class PlacesResponse(
        val results: List<PlacesSearchResult> = emptyList()
)