package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
        entity = EventEntity::class,
        parentColumns = ["id"],
        childColumns = ["eventId"],
        onDelete = ForeignKey.CASCADE
)])
data class EventImageEntity(
        @PrimaryKey
        val id: Int,
        val src: String,
        var eventId: Int = -1
)