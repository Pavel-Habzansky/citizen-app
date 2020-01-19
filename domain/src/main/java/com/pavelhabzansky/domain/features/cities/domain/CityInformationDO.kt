package com.pavelhabzansky.domain.features.cities.domain

data class CityInformationDO(
    val key: String,
    val name: String?,
    val description: String?,
    val population: Long?
)