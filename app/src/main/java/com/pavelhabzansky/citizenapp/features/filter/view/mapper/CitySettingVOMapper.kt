package com.pavelhabzansky.citizenapp.features.filter.view.mapper

import com.pavelhabzansky.citizenapp.features.filter.view.vo.CitySettingVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO

object CitySettingVOMapper : Mapper<CitySettingDO, CitySettingVO>() {

    override fun mapFrom(from: CitySettingDO) = CitySettingVO(
            enumName = from.enumName,
            name = from.name,
            enabled = from.enabled,
            countryCode = from.countryCode
    )

}