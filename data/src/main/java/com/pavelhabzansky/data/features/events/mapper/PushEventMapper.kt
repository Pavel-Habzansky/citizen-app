package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.data.features.events.entities.PushEventEntity
import com.pavelhabzansky.domain.features.events.domain.PushEvent

object PushEventMapper : MapperDirectional<PushEventEntity, PushEvent>() {

    override fun mapFrom(from: PushEventEntity) = PushEvent(
            id = from.id,
            title = from.title,
            body = from.body,
            timestamp = from.timestamp
    )

    override fun mapTo(to: PushEvent) = PushEventEntity(
            id = to.id,
            title = to.title,
            body = to.body,
            timestamp = to.timestamp
    )

}