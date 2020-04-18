package com.pavelhabzansky.domain.features.events.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.domain.PushEvent
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class LoadInboxUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<LiveData<List<PushEvent>>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<List<PushEvent>> {
        return eventsRepository.loadInbox()
    }

}