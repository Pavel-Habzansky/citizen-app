package com.pavelhabzansky.citizenapp.features.cities.search.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.LastCitySearchVO
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO

object LastSearchesVOMapper : MapperDirectional<LastSearchItemDO, LastCitySearchVO>() {

    override fun mapFrom(from: LastSearchItemDO) = LastCitySearchVO(
        key = from.key,
        name = from.name
    )

    override fun mapTo(to: LastCitySearchVO) = LastSearchItemDO(
        key = to.key,
        name = to.name
    )

}