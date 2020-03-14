package com.pavelhabzansky.citizenapp.features.place.view.mapper

import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceTypeVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.places.domain.PlaceDO

object PlacesVOMapper : Mapper<PlaceDO, PlaceVO>() {

    override fun mapFrom(from: PlaceDO) = PlaceVO(
            placeId = from.placeId,
            lat = from.lat,
            lng = from.lng,
            name = from.name,
            rating = from.rating,
            type = PlaceTypeVO.fromDomain(from.type),
            vicinity = from.vicinity,
            open = from.open
    )

}