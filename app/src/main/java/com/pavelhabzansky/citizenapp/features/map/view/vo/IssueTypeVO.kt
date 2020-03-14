package com.pavelhabzansky.citizenapp.features.map.view.vo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pavelhabzansky.citizenapp.R

enum class IssueTypeVO(val type: String, @StringRes val text: Int, @DrawableRes val icon: Int) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE", R.string.issue_type_public_damage, R.drawable.pin_pub_dmg),
    LOST_ITEM("LOST_ITEM", R.string.issue_type_lost_item, R.drawable.pin_lost_item),
    PAVEMENTS("PAVEMENTS", R.string.issue_type_pavements, R.drawable.pin_pavement),
    OTHERS("OTHERS", R.string.issue_type_others, R.drawable.pin_other),
    LIGHTS("LIGHTS", R.string.issue_type_lights, R.drawable.pin_light),
    TRASH("TRASH", R.string.issue_type_trash, R.drawable.pin_trash);

    companion object {

        fun fromString(type: String): IssueTypeVO {
            return when (type) {
                PUBLIC_DAMAGE.type -> PUBLIC_DAMAGE
                LOST_ITEM.type -> LOST_ITEM
                PAVEMENTS.type -> PAVEMENTS
                LIGHTS.type -> LIGHTS
                TRASH.type -> TRASH
                else -> OTHERS
            }
        }
    }

}