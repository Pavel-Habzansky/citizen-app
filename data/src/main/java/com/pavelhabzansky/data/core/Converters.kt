package com.pavelhabzansky.data.core

import androidx.room.TypeConverter
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.data.features.issues.model.IssueType

class Converters {

    @TypeConverter
    fun fromIssueType(type: IssueType): String {
        return type.type
    }

    @TypeConverter
    fun stringToIssueType(string: String): IssueType {
        return IssueType.fromString(type = string)
    }

    @TypeConverter
    fun stringToGps(string: String): Gps {
        val split = string.split(',')
        val lat = split.first()
        val lng = split.last()
        return Gps(lat = lat.toDouble(), lng = lng.toDouble())
    }

    @TypeConverter
    fun gpsToString(gps: Gps): String {
        return gps.toString()
    }

}