package com.pavelhabzansky.data.features.news.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey
    val title: String,
    val description: String,
    val url: String,
    val date: String,
    val read: Boolean = false
) {

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is NewsEntity) {
            return other.title == this.title
        }

        return super.equals(other)
    }

}