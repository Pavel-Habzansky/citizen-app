package com.pavelhabzansky.domain.features.issues.domain

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