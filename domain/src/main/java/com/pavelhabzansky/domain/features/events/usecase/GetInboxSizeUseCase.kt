package com.pavelhabzansky.domain.features.events.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class GetInboxSizeUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<LiveData<Int>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<Int> {
        return eventsRepository.inboxSize()
    }

}