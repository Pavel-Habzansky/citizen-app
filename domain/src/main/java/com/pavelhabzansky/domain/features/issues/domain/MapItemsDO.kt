package com.pavelhabzansky.domain.features.issues.domain

import com.pavelhabzansky.domain.features.places.domain.PlaceDO

data class MapItemsDO(
        val issues: List<IssueDO> = emptyList(),
        val places: List<PlaceDO> = emptyList()
)

fun assembleMapItems(issues: List<IssueDO> = emptyList(), places: List<PlaceDO> = emptyList()): MapItemsDO {
    return MapItemsDO(issues, places)
}