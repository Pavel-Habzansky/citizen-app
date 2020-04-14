package com.pavelhabzansky.data.features.events.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CitySettingEntity(
        @PrimaryKey
        val enumName: String,
        val name: String? = null,
        val enabled: Boolean = true,
        val countryCode: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if(other is CitySettingEntity) {
            return enumName == other.enumName
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return enumName.hashCode()
    }

}