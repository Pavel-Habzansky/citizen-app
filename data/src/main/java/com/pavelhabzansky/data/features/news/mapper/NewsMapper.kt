package com.pavelhabzansky.data.features.news.mapper

import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.domain.features.news.domain.NewsDO

object NewsMapper : Mapper<NewsEntity, NewsDO>() {

    override fun mapFrom(from: NewsEntity) = NewsDO(
        title = from.title,
        description = from.description,
        url = from.url,
        date = from.date,
        read = from.read
    )

}