package com.pavelhabzansky.data.features.news.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey
    val title: String,
    val description: String,
    val url: String,
    val date: String
)