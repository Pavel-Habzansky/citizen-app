package com.pavelhabzansky.citizenapp.features.map.view.vo

data class PlaceVO(
        val placeId: String,
        val lat: Double,
        val lng: Double,
        val name: String,
        val type: String,
        val vicinity: String
)