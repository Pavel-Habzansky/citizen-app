package com.pavelhabzansky.domain.features.cities.domain

data class CityInformationDO(
    val key: String,
    val id: String,
    val name: String,
    val description: String?,
    val population: Long?,
    val lat: Double?,
    val lng: Double?,
    val www: String,
    val logoBytes: ByteArray? = null,
    val rssFeed: String? = null,
    val rssUrl: String? = null
)