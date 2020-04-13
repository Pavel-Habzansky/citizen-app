package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.Event
import com.pavelhabzansky.data.features.events.entities.EventEntity

object EventMapper : Mapper<Event, EventEntity>() {

    override fun mapFrom(from: Event) = EventEntity(
            url = from.url,
            mainImageSrc = toSrcValid(from.mainImage.src),
            name = from.name,
            id = from.id,
            text = from.text
    )

}