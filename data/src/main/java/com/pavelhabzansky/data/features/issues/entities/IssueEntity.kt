package com.pavelhabzansky.data.features.issues.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelhabzansky.data.features.issues.model.IssueType

@Entity
data class IssueEntity(
    @PrimaryKey
    val title: String,
    val createTime: Long,
    val type: IssueType,
    val description: String,
    val lat: Double,
    val lng: Double,
    val img: String
)