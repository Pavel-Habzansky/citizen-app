package com.pavelhabzansky.data.features.issues.model

data class Gps(
    var lat: Double = 0.0,
    var lng: Double = 0.0
) {

    override fun toString(): String {
        return "$lat,$lng"
    }

}