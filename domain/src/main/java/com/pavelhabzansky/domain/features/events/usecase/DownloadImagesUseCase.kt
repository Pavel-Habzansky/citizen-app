package com.pavelhabzansky.domain.features.events.usecase

import android.graphics.Bitmap
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class DownloadImagesUseCase(
        private val issuesRepository: IIssuesRepository
): UseCase<List<Pair<String, Bitmap>>, List<String>>() {

    override suspend fun doWork(params: List<String>): List<Pair<String, Bitmap>> {
        params.forEach {
            issuesRepository.downloadImage(it)
        }
        TODO("Not yet implemented")
    }

}