package com.pavelhabzansky.domain.features.news.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class LoadNewsUseCase(
    private val newsRepository: INewsRepository
) : UseCase<Unit, LoadNewsUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        newsRepository.loadNews(force = params.force)
    }

    data class Params(
        val force: Boolean
    )

}