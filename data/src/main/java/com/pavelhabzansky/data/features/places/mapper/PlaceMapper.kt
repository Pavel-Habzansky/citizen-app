package com.pavelhabzansky.data.features.places.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.PlacesSearchResult
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO

object PlaceMapper : Mapper<PlaceEntity, PlaceDO>() {

    override fun mapFrom(from: PlaceEntity) = PlaceDO(
            placeId = from.placeId,
            lat = from.location.lat,
            lng = from.location.lng,
            name = from.name,
            rating = from.rating,
            type = PlaceTypeDO.fromString(type = from.type),
            vicinity = from.vicinity,
            open = from.open
    )

    fun mapApiToEntity(api: PlacesSearchResult) = PlaceEntity(
            placeId = requireNotNull(api.placeId),
            location = requireNotNull(api.geometry?.location),
            name = requireNotNull(api.name),
            vicinity = requireNotNull(api.vicinity),
            open = api.openingHours?.openNow ?: false,
            rating = api.rating
    )

}