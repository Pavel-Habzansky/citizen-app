package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.Locality
import com.pavelhabzansky.data.features.events.entities.LocalityEntity

object LocalityMapper : Mapper<Locality, LocalityEntity>() {

    override fun mapFrom(from: Locality) = LocalityEntity(
            id = from.id,
            name = from.name,
            enum = from.enum,
            countryId = from.country.id,
            countryCode = from.country.shortCode
    )

}