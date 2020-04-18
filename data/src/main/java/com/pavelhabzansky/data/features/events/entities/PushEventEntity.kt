package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PushEventEntity(
        @PrimaryKey
        val id: String,
        val title: String,
        val body: String,
        val timestamp: Long
)