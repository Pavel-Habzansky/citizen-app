package com.pavelhabzansky.domain.features.issues.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO

interface IIssuesRepository {

    suspend fun fetchIssues()

    suspend fun getAllIssues(): LiveData<List<IssueDO>>

    suspend fun getBoundIssues(bounds: Bounds): List<IssueDO>

    suspend fun createIssue(issue: IssueDO, attachment: Bitmap)

}