package com.pavelhabzansky.citizenapp.features.filter.view.vo

data class CitySettingVO(
        val enumName: String,
        val name: String? = null,
        var enabled: Boolean = true,
        val countryCode: String? = null
)