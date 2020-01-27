package com.pavelhabzansky.citizenapp.features.cities.detail.view.vo

data class CityInformationVO(
    val key: String,
    val name: String?,
    val description: String?,
    val population: Long?,
    val lat: Double?,
    val lng: Double?,
    val www: String,
    val logoBytes: ByteArray? = null
)