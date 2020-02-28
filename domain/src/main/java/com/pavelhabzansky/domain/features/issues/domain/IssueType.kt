package com.pavelhabzansky.domain.features.issues.domain

enum class IssueType(val type: String) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE"),
    LOST_ITEM("LOST_ITEM"),
    PAVEMENTS("PAVEMENTS"),
    OTHERS("OTHERS"),
    LIGHTS("LIGHTS"),
    TRASH("TRASH"),
    UNKNOWN("UNKNOWN");

    companion object {

        fun fromString(type: String): IssueType {
            return when(type) {
                PUBLIC_DAMAGE.type -> PUBLIC_DAMAGE
                LOST_ITEM.type -> LOST_ITEM
                PAVEMENTS.type -> PAVEMENTS
                OTHERS.type -> OTHERS
                LIGHTS.type -> LIGHTS
                TRASH.type -> TRASH
                else -> UNKNOWN
            }
        }

    }

}