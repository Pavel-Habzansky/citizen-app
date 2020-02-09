package com.pavelhabzansky.domain.features.news.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class LoadCachedNewsUseCase(
    private val newsRepository: INewsRepository
) : UseCase<LiveData<List<NewsDO>>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<List<NewsDO>> {
        return newsRepository.loadCachedNews()
    }

}