package com.pavelhabzansky.domain.features.issues.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class FetchIssuesUseCase(
    private val issueRepository: IIssuesRepository
) : UseCase<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        issueRepository.fetchIssues()
    }

}