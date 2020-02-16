package com.pavelhabzansky.data.features.issues.repository

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.data.features.issues.mapper.IssueMapper
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.data.features.issues.model.IssueType
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import timber.log.Timber

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
                    val createTime = if (it.child("createTime").toString().isDigitsOnly()) {
                        it.child("createTime").value.toString().toLong()
                    } else {
                        0L
                    }

                    val description = it.child("description").value.toString()
                    val img = it.child("img").value.toString()
                    val title = it.child("title").value.toString()
                    val type = it.child("type").value.toString()

                    val gps = it.child("gps").getValue(Gps::class.java)

                    val lat = gps?.lat
                    val lng = gps?.lng

                    val entity = IssueEntity(
                        title = title,
                        description = description,
                        createTime = createTime,
                        type = IssueType.fromString(type = type),
                        lat = lat ?: 0.0,
                        lng = lng ?: 0.0,
                        img = img
                    )

                    Timber.i(entity.toString())
                }

                issueDao.insertAll(issues)
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
    }

    override suspend fun getAllIssues(): LiveData<List<IssueDO>> {
        val entities = issueDao.getAll()

        return Transformations.map(entities) {
            it.map { IssueMapper.mapFrom(from = it) }
        }
    }

}