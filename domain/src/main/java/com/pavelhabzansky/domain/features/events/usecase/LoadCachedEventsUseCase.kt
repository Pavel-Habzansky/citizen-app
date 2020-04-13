package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.domain.ScheduleDO
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class LoadCachedEventsUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<List<ScheduleDO>, Unit>() {

    override suspend fun doWork(params: Unit): List<ScheduleDO> {
        return eventsRepository.loadEvents()
    }

}