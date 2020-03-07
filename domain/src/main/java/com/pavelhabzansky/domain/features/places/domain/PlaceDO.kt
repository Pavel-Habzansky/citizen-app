package com.pavelhabzansky.domain.features.places.domain

data class PlaceDO(
        val placeId: String,
        val lat: Double,
        val lng: Double,
        val name: String,
        val type: String,
        val vicinity: String
)