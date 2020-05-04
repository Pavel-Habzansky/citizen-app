package com.pavelhabzansky.domain.features.issues.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.domain.MyIssueDO

interface IIssuesRepository {

    suspend fun fetchIssues()

    suspend fun loadIssues(): LiveData<List<IssueDO>>

    suspend fun getAllIssues(): LiveData<List<IssueDO>>

    suspend fun getBoundIssues(): List<IssueDO>

    suspend fun createIssue(issue: IssueDO, attachment: Bitmap)

    suspend fun downloadImage(name: String): LiveData<ByteArray>

    suspend fun loadUserIssues(): LiveData<List<MyIssueDO>>

}