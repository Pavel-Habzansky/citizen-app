package com.pavelhabzansky.data.features.issues.model

enum class IssueType(val type: String) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE"),
    LOST_ITEM("LOST_ITEM");


    companion object {

        fun fromString(type: String): IssueType {
            return when(type) {
                PUBLIC_DAMAGE.type -> PUBLIC_DAMAGE
                else -> LOST_ITEM
            }
        }

    }

}