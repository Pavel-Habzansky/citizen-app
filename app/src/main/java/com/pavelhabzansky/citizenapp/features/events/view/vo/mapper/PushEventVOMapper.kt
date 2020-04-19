package com.pavelhabzansky.citizenapp.features.events.view.vo.mapper

import com.pavelhabzansky.citizenapp.features.events.view.vo.PushEventVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.events.domain.PushEvent

object PushEventVOMapper : Mapper<PushEvent, PushEventVO>() {

    override fun mapFrom(from: PushEvent) = PushEventVO(
            id = from.id,
            timestamp = from.timestamp,
            title = from.title,
            body = from.body,
            url = from.url
    )

}