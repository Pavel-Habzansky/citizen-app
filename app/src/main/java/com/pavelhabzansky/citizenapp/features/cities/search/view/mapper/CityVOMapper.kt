package com.pavelhabzansky.citizenapp.features.cities.search.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.CityVO
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.domain.features.cities.domain.CityDO

object CityVOMapper : MapperDirectional<CityDO, CityVO>() {

    override fun mapFrom(from: CityDO) = CityVO(
        key = from.key,
        name = from.name,
        id = from.id
    )

    override fun mapTo(to: CityVO) = CityDO(
        key = to.key,
        name = to.name,
        id = to.id
    )

}