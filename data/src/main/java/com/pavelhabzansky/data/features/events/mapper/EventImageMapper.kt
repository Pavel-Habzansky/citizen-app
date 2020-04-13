package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.EventImage
import com.pavelhabzansky.data.features.events.entities.EventImageEntity

object EventImageMapper : Mapper<EventImage, EventImageEntity>() {

    override fun mapFrom(from: EventImage) = EventImageEntity(
            id = from.id,
            src = toSrcValid(from.src)
    )

}

fun toSrcValid(src: String): String {
    val index = src.indexOfFirst { it == '%' }
    return src.replaceRange(index..index + 2, "383")
}

