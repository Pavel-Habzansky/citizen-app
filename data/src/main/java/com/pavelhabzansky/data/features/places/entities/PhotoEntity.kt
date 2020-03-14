package com.pavelhabzansky.data.features.places.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = PlaceEntity::class,
                    parentColumns = ["placeId"],
                    childColumns = ["placeId"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class PhotoEntity(
        @PrimaryKey
        val photoRef: String,
        var height: Int? = null,
        var width: Int? = null,
        var placeId: String? = null
)