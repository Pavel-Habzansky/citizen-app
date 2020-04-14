package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.data.features.events.entities.CitySettingEntity
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO

object CitySettingMapper : MapperDirectional<CitySettingEntity, CitySettingDO>() {

    override fun mapFrom(from: CitySettingEntity) = CitySettingDO(
            enumName = from.enumName,
            name = from.name,
            enabled = from.enabled,
            countryCode = from.countryCode
    )

    override fun mapTo(to: CitySettingDO) = CitySettingEntity(
            enumName = to.enumName,
            name = to.name,
            enabled = to.enabled,
            countryCode = to.countryCode
    )

}