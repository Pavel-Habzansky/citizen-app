package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = LocalityEntity::class, parentColumns = ["id"], childColumns = ["venueLocalityId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = EventEntity::class, parentColumns = ["id"], childColumns = ["eventId"], onDelete = ForeignKey.CASCADE)
])
data class ScheduleEntity(
        @PrimaryKey
        val id: Int,
        val cancelled: Boolean,
        val currency: String,
        val eventId: Int,
        val pricing: String,
        val start: String,
        val venueId: Int,
        val venueLocalityId: Int,
        val url: String,
        val timestampStart: Long
)