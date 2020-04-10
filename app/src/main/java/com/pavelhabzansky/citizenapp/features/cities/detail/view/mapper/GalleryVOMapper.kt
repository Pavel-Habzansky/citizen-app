package com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper

import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityGalleryItemVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.cities.domain.CityGalleryItemDO

object GalleryVOMapper : Mapper<CityGalleryItemDO, CityGalleryItemVO>() {

    override fun mapFrom(from: CityGalleryItemDO) = CityGalleryItemVO(from.image)

}