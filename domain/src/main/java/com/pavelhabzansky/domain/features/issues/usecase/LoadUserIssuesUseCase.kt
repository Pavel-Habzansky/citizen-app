package com.pavelhabzansky.domain.features.issues.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.domain.MyIssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class LoadUserIssuesUseCase(
        private val issueRepository: IIssuesRepository
) : UseCase<LiveData<List<MyIssueDO>>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<List<MyIssueDO>> {
        return issueRepository.loadUserIssues()
    }

}