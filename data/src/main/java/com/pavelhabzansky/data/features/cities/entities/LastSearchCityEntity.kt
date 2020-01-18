package com.pavelhabzansky.data.features.cities.entities

import androidx.room.Entity

@Entity(primaryKeys = ["key"])
data class LastSearchCityEntity(
    val key: String,
    val name: String,
    val timestamp: Long
)