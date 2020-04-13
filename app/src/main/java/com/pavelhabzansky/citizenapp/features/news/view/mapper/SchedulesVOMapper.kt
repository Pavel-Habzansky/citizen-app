package com.pavelhabzansky.citizenapp.features.news.view.mapper

import android.graphics.BitmapFactory
import com.pavelhabzansky.citizenapp.features.news.view.vo.ScheduleViewObject
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.events.domain.ScheduleDO

object SchedulesVOMapper : Mapper<ScheduleDO, ScheduleViewObject>() {

    override fun mapFrom(from: ScheduleDO) = ScheduleViewObject(
            id = from.id,
            name = from.name,
            text = from.text,
            mainImageUrl = from.mainImageUrl,
            images = from.images,
            date = from.date,
            url = from.url,
            bitmap = BitmapFactory.decodeByteArray(from.image, 0, from.image.size),
            pricing = from.pricing,
            price = "Cena: " + from.pricing + " " + from.currency
    )


}