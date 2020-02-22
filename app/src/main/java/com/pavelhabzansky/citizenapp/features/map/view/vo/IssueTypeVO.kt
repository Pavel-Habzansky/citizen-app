package com.pavelhabzansky.citizenapp.features.map.view.vo

enum class IssueTypeVO(val type: String) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE"),
    LOST_ITEM("LOST_ITEM"),
    UNKNOWN("UNKNOWN");

    companion object {

        fun fromString(type: String): IssueTypeVO {
            return when (type) {
                PUBLIC_DAMAGE.type -> PUBLIC_DAMAGE
                LOST_ITEM.type -> LOST_ITEM
                else -> UNKNOWN
            }
        }

    }

}