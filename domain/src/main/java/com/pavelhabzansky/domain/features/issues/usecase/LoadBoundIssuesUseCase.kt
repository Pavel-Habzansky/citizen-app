package com.pavelhabzansky.domain.features.issues.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class LoadBoundIssuesUseCase(
    private val issueRepository: IIssuesRepository
) : UseCase<List<IssueDO>, Bounds>() {

    override suspend fun doWork(params: Bounds): List<IssueDO> {
        return issueRepository.getBoundIssues(bounds = params)
    }

}