package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.domain.PushEvent
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class StorePushEventUseCase(
        private val eventRepository: IEventsRepository
) : UseCase<Unit, PushEvent>() {

    override suspend fun doWork(params: PushEvent) {
        eventRepository.storePushEvent(params)
    }

}