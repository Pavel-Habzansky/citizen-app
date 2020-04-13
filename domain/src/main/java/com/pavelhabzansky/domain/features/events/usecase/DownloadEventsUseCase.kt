package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class DownloadEventsUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<Unit, Boolean>() {

    override suspend fun doWork(params: Boolean) {
        eventsRepository.downloadEvents(params)
    }

}