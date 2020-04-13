package com.pavelhabzansky.data.features.api

data class Locality(
        val id: Int,
        val name: String,
        val enum: String,
        val country: Country
)