package com.pavelhabzansky.citizenapp.features.cities.search.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.AutoCompleteItem
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.cities.domain.CityDO

object AutoCompleteMapper : Mapper<CityDO, AutoCompleteItem>() {

    override fun mapFrom(from: CityDO) = AutoCompleteItem(
        key = from.key,
        name = from.name
    )

}