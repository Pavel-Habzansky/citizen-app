package com.pavelhabzansky.domain.features.news.domain

data class NewsDO(
    val title: String,
    val description: String,
    val url: String,
    val date: String,
    val read: Boolean = false
)