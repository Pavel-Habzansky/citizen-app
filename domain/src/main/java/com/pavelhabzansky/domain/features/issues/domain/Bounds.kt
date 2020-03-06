package com.pavelhabzansky.domain.features.issues.domain

import com.pavelhabzansky.domain.core.isBetween

data class Bounds(
    val west: Double,
    val north: Double,
    val east: Double,
    val south: Double
) {

    fun isInBounds(lat: Double, lng: Double): Boolean {
        return lat.isBetween(north, south) && lng.isBetween(west, east)
    }


}