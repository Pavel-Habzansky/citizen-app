package com.pavelhabzansky.domain.features.events.domain

data class PushEvent(
        val id: String,
        val title: String,
        val body: String,
        val timestamp: Long,
        val url: String? = null
)