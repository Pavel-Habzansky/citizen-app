package com.pavelhabzansky.domain.features.news.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class ConnectFirebaseUseCase(
    private val newsRepository: INewsRepository
) : UseCase<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        newsRepository.connectFirebase()
    }

}