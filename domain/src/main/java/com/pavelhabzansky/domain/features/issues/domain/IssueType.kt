package com.pavelhabzansky.domain.features.issues.domain

enum class IssueType(val type: String) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE"),
    LOST_ITEM("LOST_ITEM"),
    UNKNOWN("UNKNOWN");

    companion object {

        fun fromString(type: String): IssueType {
            return when(type) {
                PUBLIC_DAMAGE.type -> PUBLIC_DAMAGE
                LOST_ITEM.type -> LOST_ITEM
                else -> UNKNOWN
            }
        }

    }

}