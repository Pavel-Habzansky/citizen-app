package com.pavelhabzansky.data.features.api

data class Schedule(
        val id: Int,
        val cancelled: Boolean,
        val currency: String,
        val eventId: Int,
        val pricing: String,
        val start: String,
        val venueId: Int,
        val venueLocality: Locality,
        val url: String
)