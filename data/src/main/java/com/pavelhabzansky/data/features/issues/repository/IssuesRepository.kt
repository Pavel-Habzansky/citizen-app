package com.pavelhabzansky.data.features.issues.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.core.CONTENT_TYPE_JPG
import com.pavelhabzansky.data.features.api.Issue
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.data.features.issues.mapper.IssueMapper
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class IssuesRepository(
    private val issuesReference: DatabaseReference,
    private val storageReference: StorageReference,
    private val issueDao: IssueDao
) : IIssuesRepository {

    override suspend fun fetchIssues() {
        issuesReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val issues = mutableListOf<IssueEntity>()
                snapshot.children.forEach {
                    val issue = it.getValue(Issue::class.java)

                    val gps = it.child("gps").getValue(Gps::class.java)

                    issue?.let {
                        val entity = IssueMapper.mapApiToIssueEntity(it)

                        entity.lat = gps?.lat ?: 0.0
                        entity.lng = gps?.lng ?: 0.0

                        issues.add(entity)
                    }
                }

                runBlocking(Dispatchers.IO) {
                    issueDao.removeAll()
                    issueDao.insertAll(issues)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
    }

    override suspend fun getBoundIssues(bounds: Bounds): List<IssueDO> {
        val issues = issueDao.getAllInBounds()
        return issues
            .filter { bounds.isInBounds(it.lat, it.lng) }
            .map { IssueMapper.mapFrom(from = it) }
    }

    override suspend fun getAllIssues(): LiveData<List<IssueDO>> {
        val entities = issueDao.getAll()

        return Transformations.map(entities) {
            it.map { IssueMapper.mapFrom(from = it) }
        }
    }

    override suspend fun createIssue(issue: IssueDO, attachment: Bitmap) {
        val issueApiObj = IssueMapper.mapDomToApi(dom = issue)

        val issueReference = issuesReference.push()
        issueApiObj.img = issueReference.key + ".jpg"
        issueReference.setValue(issueApiObj)

        val metaData = StorageMetadata.Builder()
            .setContentType(CONTENT_TYPE_JPG)
            .build()

        val stream = ByteArrayOutputStream()
        attachment.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        storageReference.child("${issueReference.key}.jpg").putBytes(bytes, metaData)
    }

}