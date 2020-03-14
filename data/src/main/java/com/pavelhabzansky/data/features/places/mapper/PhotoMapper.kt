package com.pavelhabzansky.data.features.places.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.PlacePhoto
import com.pavelhabzansky.data.features.places.entities.PhotoEntity

object PhotoMapper : Mapper<PlacePhoto, PhotoEntity>() {

    override fun mapFrom(from: PlacePhoto) = PhotoEntity(
            photoRef = requireNotNull(from.photoRef),
            height = from.height,
            width = from.width
    )

}