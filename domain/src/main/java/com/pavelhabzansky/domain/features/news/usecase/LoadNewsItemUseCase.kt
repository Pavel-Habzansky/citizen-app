package com.pavelhabzansky.domain.features.news.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class LoadNewsItemUseCase(
    private val newsRepository: INewsRepository
) : UseCase<LiveData<NewsDO>, LoadNewsItemUseCase.Params>() {

    override suspend fun doWork(params: Params): LiveData<NewsDO> {
        return newsRepository.loadNewsItem(title = params.title)
    }

    data class Params(
        val title: String
    )
}