package com.pavelhabzansky.data.features.cities.entities

import androidx.room.Entity

@Entity(primaryKeys = ["key"])
data class CityEntity(
    var key: String = "",
    var id: String = "",
    var name: String = "",
    var rssFeed: String = "",
    var rssUrl: String = "",

    var residential: Boolean = false
)