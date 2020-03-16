package com.pavelhabzansky.domain.features.places.domain

data class PlaceDO(
        val placeId: String,
        val lat: Double,
        val lng: Double,
        val name: String,
        val rating: Double?,
        val type: PlaceTypeDO,
        val open: Boolean?,
        val vicinity: String
)