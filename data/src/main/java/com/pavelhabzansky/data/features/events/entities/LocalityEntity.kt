package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = CountryEntity::class,
            parentColumns = ["id"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.CASCADE)
])
data class LocalityEntity(
        @PrimaryKey
        var id: Int = -1,
        var name: String = "EMPTY",
        var enum: String = "EMPTY",
        var countryId: Int = -1
)