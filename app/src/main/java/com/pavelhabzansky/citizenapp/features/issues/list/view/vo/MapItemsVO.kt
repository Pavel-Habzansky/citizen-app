package com.pavelhabzansky.citizenapp.features.issues.list.view.vo

import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.citizenapp.features.place.view.mapper.PlacesVOMapper
import com.pavelhabzansky.domain.features.issues.domain.MapItemsDO

data class MapItemsVO(
        val mapItems: List<MapItem>
)

fun mapItemsFromDomain(mapItems: MapItemsDO): MapItemsVO {
    return MapItemsVO(
            mapItems.issues.map { IssueVOMapper.mapTo(it) } +
                    mapItems.places.map { PlacesVOMapper.mapFrom(it) }
    )
}