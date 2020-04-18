package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class DeletePushEventUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<Unit, String>() {

    override suspend fun doWork(params: String) {
        eventsRepository.removePushEvent(params)
    }

}