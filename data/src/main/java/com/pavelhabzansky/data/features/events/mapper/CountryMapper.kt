package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.Country
import com.pavelhabzansky.data.features.events.entities.CountryEntity

object CountryMapper : Mapper<Country, CountryEntity>() {

    override fun mapFrom(from: Country) = CountryEntity(
            id = from.id,
            name = from.name,
            enum = from.enum,
            shortCode = from.shortCode
    )

}