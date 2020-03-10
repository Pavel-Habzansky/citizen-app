package com.pavelhabzansky.data.features.places.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceSettingsEntity(
        @PrimaryKey
        val type: String,
        val enabled: Boolean = true
)