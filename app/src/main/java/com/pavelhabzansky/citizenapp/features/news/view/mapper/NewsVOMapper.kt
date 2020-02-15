package com.pavelhabzansky.citizenapp.features.news.view.mapper

import com.pavelhabzansky.citizenapp.core.fromRssDate
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.news.domain.NewsDO

object NewsVOMapper : Mapper<NewsDO, NewsItemViewObject>() {

    override fun mapFrom(from: NewsDO) = NewsItemViewObject(
        title = from.title,
        description = from.description,
        url = from.url,
        date = from.date.fromRssDate(),
        read = from.read
    )

}