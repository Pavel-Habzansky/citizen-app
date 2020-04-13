package com.pavelhabzansky.data.features.events.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.api.Schedule
import com.pavelhabzansky.data.features.events.entities.ScheduleEntity
import java.sql.Timestamp

object ScheduleMapper : Mapper<Schedule, ScheduleEntity>() {

    override fun mapFrom(from: Schedule) = ScheduleEntity(
            id = from.id,
            cancelled = from.cancelled,
            currency = from.currency,
            eventId = from.eventId,
            pricing = from.pricing,
            start = from.start,
            venueId = from.venueId,
            venueLocalityId = from.venueLocality.id,
            url = from.url,
            timestampStart = Timestamp.valueOf(from.start).time
    )

}