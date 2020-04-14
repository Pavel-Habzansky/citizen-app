package com.pavelhabzansky.citizenapp.features.news.view.vo

import android.graphics.Bitmap

data class ScheduleViewObject(
        val id: Int,
        val name: String,
        val text: String,
        val mainImageUrl: String,
        val images: List<String>,
        val date: String,
        val url: String,
        var bitmap: Bitmap? = null,
        val pricing: String,
        val price: String,
        val locality: String
)