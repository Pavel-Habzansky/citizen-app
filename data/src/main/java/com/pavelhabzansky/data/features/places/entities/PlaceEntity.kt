package com.pavelhabzansky.data.features.places.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelhabzansky.data.features.issues.model.Gps

@Entity
data class PlaceEntity(
        @PrimaryKey
        val placeId: String,
        val location: Gps,
        val name: String,
        var type: String = "",
        val rating: Double = 5.0,
        val open: Boolean = false,
        val vicinity: String
)