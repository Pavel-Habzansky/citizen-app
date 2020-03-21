package com.pavelhabzansky.domain.features.news.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class LoadTouristNewsUseCase(
        private val newsRepository: INewsRepository
) : UseCase<LiveData<List<NewsDO>>, LoadTouristNewsUseCase.Params>() {

    override suspend fun doWork(params: Params): LiveData<List<NewsDO>> {
        return newsRepository.loadTouristNews(params.lat, params.lng)
    }

    data class Params(
            val lat: Double,
            val lng: Double
    )

}