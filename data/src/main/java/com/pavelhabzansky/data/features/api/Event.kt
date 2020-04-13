package com.pavelhabzansky.data.features.api

data class Event(
        val images: List<EventImage>,
        val url: String,
        val mainImage: EventImage,
        val name: String,
        val id: Int,
        val text: String
)