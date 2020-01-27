package com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO

object CityInformationVOMapper : Mapper<CityInformationDO, CityInformationVO>() {

    override fun mapFrom(from: CityInformationDO) = CityInformationVO(
        key = from.key,
        name = from.key,
        description = from.description,
        population = from.population,
        logoBytes = from.logoBytes
    )

}