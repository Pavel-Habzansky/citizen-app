package com.pavelhabzansky.domain.features.events.domain

data class CitySettingDO(
        val enumName: String,
        val name: String? = null,
        val enabled: Boolean = true,
        val countryCode: String? = null
)