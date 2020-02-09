package com.pavelhabzansky.domain.features.cities.domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CityDO(
    var key: String = "",
    var id: String = "",
    var name: String = "",
    var residential: Boolean = false,
    val rssFeed: String? = null,
    val rssUrl: String? = null
)