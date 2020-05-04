package com.pavelhabzansky.data.features.issues.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserIssueEntity(
        @PrimaryKey
       val key: String
)