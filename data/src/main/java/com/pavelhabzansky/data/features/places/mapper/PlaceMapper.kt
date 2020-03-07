package com.pavelhabzansky.data.features.places.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.PlacesSearchResult
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.domain.features.places.domain.PlaceDO

object PlaceMapper : Mapper<PlaceEntity, PlaceDO>() {

    override fun mapFrom(from: PlaceEntity) = PlaceDO(
            placeId = from.placeId,
            lat = from.location.lat,
            lng = from.location.lng,
            name = from.name,
            // TODO Map as list of PlaceTypes
            type = from.type,
            vicinity = from.vicinity
    )

    fun mapApiToEntity(api: PlacesSearchResult) = PlaceEntity(
            placeId = requireNotNull(api.placeId),
            location = requireNotNull(api.geometry?.location),
            name = requireNotNull(api.name),
            type = api.types.joinToString(","),
            vicinity = requireNotNull(api.vicinity)
    )

}