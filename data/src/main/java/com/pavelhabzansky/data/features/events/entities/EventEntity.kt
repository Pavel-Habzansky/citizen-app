package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventEntity(
        val url: String,
        val mainImageSrc: String,
        val name: String,
        @PrimaryKey
        val id: Int,
        val text: String
)