package com.pavelhabzansky.domain.features.issues.usecase

import android.graphics.Bitmap
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository

class CreateIssueUseCase(
    private val issueRepository: IIssuesRepository
) : UseCase<Unit, CreateIssueUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        issueRepository.createIssue(params.issueDom, params.attachment)
    }

    data class Params(
        val issueDom: IssueDO,
        val attachment: Bitmap
    )
}