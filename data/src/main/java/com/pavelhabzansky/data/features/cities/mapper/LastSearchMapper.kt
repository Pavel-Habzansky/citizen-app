package com.pavelhabzansky.data.features.cities.mapper

import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO

object LastSearchMapper : MapperDirectional<LastSearchCityEntity, LastSearchItemDO>() {

    override fun mapFrom(from: LastSearchCityEntity) = LastSearchItemDO(
        key = from.key,
        name = from.name
    )

    override fun mapTo(to: LastSearchItemDO) = LastSearchCityEntity(
        key = to.key,
        name = to.name,
        timestamp = System.currentTimeMillis()
    )


}