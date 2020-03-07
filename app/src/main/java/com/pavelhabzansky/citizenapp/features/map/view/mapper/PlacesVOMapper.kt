package com.pavelhabzansky.citizenapp.features.map.view.mapper

import com.pavelhabzansky.citizenapp.features.map.view.vo.PlaceVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.places.domain.PlaceDO

object PlacesVOMapper : Mapper<PlaceDO, PlaceVO>() {

    override fun mapFrom(from: PlaceDO) = PlaceVO(
            placeId = from.placeId,
            lat = from.lat,
            lng = from.lng,
            name = from.name,
            type = from.type,
            vicinity = from.vicinity
    )

}