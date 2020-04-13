package com.pavelhabzansky.domain.features.events.usecase

import android.graphics.Bitmap
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class DownloadImageUseCase(
        private val eventRepository: IEventsRepository
) : UseCase<ByteArray, String>() {

    override suspend fun doWork(params: String): ByteArray {
        return eventRepository.downloadImage(params)
    }

}