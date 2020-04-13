package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class DownloadGalleryUseCase(
        private val eventRepository: IEventsRepository
) : UseCase<List<ByteArray>, List<String>>() {

    override suspend fun doWork(params: List<String>): List<ByteArray> {
        return eventRepository.downloadGallery(params)
    }
}