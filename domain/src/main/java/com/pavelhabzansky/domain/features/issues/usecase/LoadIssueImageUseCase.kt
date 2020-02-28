package com.pavelhabzansky.domain.features.issues.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class LoadIssueImageUseCase(
    private val issuesRepository: IIssuesRepository
) : UseCase<LiveData<ByteArray>, LoadIssueImageUseCase.Params>() {

    override suspend fun doWork(params: Params): LiveData<ByteArray> {
        return issuesRepository.downloadImage(name = params.name)
    }

    data class Params(
        val name: String
    )
}