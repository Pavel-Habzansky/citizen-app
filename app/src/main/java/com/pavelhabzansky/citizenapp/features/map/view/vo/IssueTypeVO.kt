package com.pavelhabzansky.citizenapp.features.map.view.vo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pavelhabzansky.citizenapp.R

enum class IssueTypeVO(val type: String, @StringRes val text: Int, @DrawableRes val icon: Int) {
    PUBLIC_DAMAGE("PUBLIC_DAMAGE", R.string.issue_type_public_damage, R.drawable.ic_pub_dmg_24px),
    LOST_ITEM("LOST_ITEM", R.string.issue_type_lost_item, R.drawable.ic_lost_item_24px),
    PAVEMENTS("PAVEMENTS", R.string.issue_type_pavements, R.drawable.ic_pavement_24px),
    OTHERS("OTHERS", R.string.issue_type_others, R.drawable.ic_other_24px),
    LIGHTS("LIGHTS", R.string.issue_type_lights, R.drawable.ic_light_24px),
    TRASH("TRASH", R.string.issue_type_trash, R.drawable.ic_trash_24px);

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