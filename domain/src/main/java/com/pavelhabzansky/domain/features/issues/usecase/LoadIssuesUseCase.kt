package com.pavelhabzansky.domain.features.issues.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class LoadIssuesUseCase(
        private val issuesRepository: IIssuesRepository
) : UseCase<List<IssueDO>, Unit>() {

    override suspend fun doWork(params: Unit): List<IssueDO> {
        return issuesRepository.loadIssues()
    }

}