package com.pavelhabzansky.domain.features.news.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class LoadNewsForCityUseCase(
    private val newsRepository: INewsRepository
) : UseCase<List<NewsDO>, LoadNewsForCityUseCase.Params>() {

    override suspend fun doWork(params: Params): List<NewsDO> {
        return newsRepository.getNews(city = params.city)
    }

    data class Params(
        val city: CityInformationDO
    )
}