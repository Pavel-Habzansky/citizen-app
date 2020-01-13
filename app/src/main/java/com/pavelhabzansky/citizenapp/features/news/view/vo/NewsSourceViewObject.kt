package com.pavelhabzansky.citizenapp.features.news.view.vo

data class NewsSourceViewObject(
    val sourceName: String,
    val sourceId: String,
    // Possibly build URL inside data layer before calling
    val url: String
)