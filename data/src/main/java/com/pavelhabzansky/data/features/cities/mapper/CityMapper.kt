package com.pavelhabzansky.data.features.cities.mapper

import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.data.features.cities.entities.CityEntity
import com.pavelhabzansky.domain.features.cities.domain.CityDO

object CityMapper : MapperDirectional<CityEntity, CityDO>() {

    override fun mapFrom(from: CityEntity) = CityDO(
        key = from.key,
        id = from.id,
        name = from.name,
        residential = from.residential
    )

    override fun mapTo(to: CityDO) = CityEntity(
        key = to.key,
        id = to.id,
        name = to.name,
        residential = to.residential
    )

}