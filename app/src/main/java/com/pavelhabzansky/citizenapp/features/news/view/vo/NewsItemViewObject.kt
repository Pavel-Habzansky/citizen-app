package com.pavelhabzansky.citizenapp.features.news.view.vo

import java.util.*

data class NewsItemViewObject(
    val title: String,
    val description: String,
    val url: String,
    val date: Date?,
    val read: Boolean = false
)