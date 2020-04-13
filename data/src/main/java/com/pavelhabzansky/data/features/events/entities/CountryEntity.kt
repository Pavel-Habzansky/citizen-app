package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryEntity(
        @PrimaryKey
        var id: Int = -1,
        var name: String = "EMPTY",
        var enum: String = "EMPTY",
        var shortCode: String = "EMPTY"
)