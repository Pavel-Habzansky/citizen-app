package com.pavelhabzansky.citizenapp.features.events.view.vo

data class PushEventVO(
        val id: String,
        val title: String,
        val body: String,
        val timestamp: Long,
        val url: String? = null
)