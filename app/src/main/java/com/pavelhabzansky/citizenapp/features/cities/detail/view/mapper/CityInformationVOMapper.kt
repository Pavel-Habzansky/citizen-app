package com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO

object CityInformationVOMapper : MapperDirectional<CityInformationDO, CityInformationVO>() {

    override fun mapFrom(from: CityInformationDO) = CityInformationVO(
            key = from.key,
            id = from.id,
            name = from.name,
            nameEn = from.nameEn,
            description = from.description,
            descriptionEn = from.descriptionEn,
            population = from.population,
            logoBytes = from.logoBytes,
            lat = from.lat,
            lng = from.lng,
            www = from.www,
            rssFeed = from.rssFeed,
            rssUrl = from.rssUrl,
            residential = from.residential
    )

    override fun mapTo(to: CityInformationVO) = CityInformationDO(
            key = to.key,
            id = to.id,
            residential = to.residential,
            name = to.name,
            nameEn = to.nameEn,
            description = to.description,
            descriptionEn = to.descriptionEn,
            population = to.population,
            logoBytes = to.logoBytes,
            lat = to.lat,
            lng = to.lng,
            www = to.www,
            rssFeed = to.rssFeed,
            rssUrl = to.rssUrl
    )

}