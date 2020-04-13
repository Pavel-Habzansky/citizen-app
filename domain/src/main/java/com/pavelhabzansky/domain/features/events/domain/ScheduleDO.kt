package com.pavelhabzansky.domain.features.events.domain

data class ScheduleDO(
        val id: Int,
        val name: String,
        val text: String,
        val mainImageUrl: String,
        val images: List<String>,
        val date: String,
        val url: String,
        val image: ByteArray,
        val pricing: String,
        val currency: String
)