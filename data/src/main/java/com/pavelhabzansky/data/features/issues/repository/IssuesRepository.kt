package com.pavelhabzansky.data.features.issues.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.core.CONTENT_TYPE_JPG
import com.pavelhabzansky.data.core.IMG_MAX_SIZE
import com.pavelhabzansky.data.core.transform
import com.pavelhabzansky.data.features.api.Issue
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.dao.UserIssueDao
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.data.features.issues.entities.UserIssueEntity
import com.pavelhabzansky.data.features.issues.mapper.IssueMapper
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.data.features.settings.dao.IssueSettingsDao
import com.pavelhabzansky.data.features.settings.entities.IssueSettingsEntity
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.issues.domain.MyIssueDO
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class IssuesRepository(
        private val issuesReference: DatabaseReference,
        private val storageReference: StorageReference,
        private val issueDao: IssueDao,
        private val issueSettingsDao: IssueSettingsDao,
        private val userIssueDao: UserIssueDao
) : IIssuesRepository {

    override suspend fun fetchIssues() {
        if (issueSettingsDao.getCount() != IssueType.values().size) {
            populateIssueSettings()
        }

        val issueTypes = issueSettingsDao.getAllEnabled().map { it.type }

        issuesReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val issues = mutableListOf<IssueEntity>()
                snapshot.children.forEach {
                    val issue = it.getValue(Issue::class.java)
                    if (issueTypes.contains(issue?.type)) {
                        val gps = it.child("gps").getValue(Gps::class.java)

                        issue?.let {
                            val entity = IssueMapper.mapApiToIssueEntity(it)

                            entity.lat = gps?.lat ?: 0.0
                            entity.lng = gps?.lng ?: 0.0

                            issues.add(entity)
                        }
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

    override suspend fun getBoundIssues(): List<IssueDO> {
        val issues = issueDao.getAllInBounds()
        return issues.map { IssueMapper.mapFrom(from = it) }
    }

    override suspend fun getAllIssues(): LiveData<List<IssueDO>> {
        val entities = issueDao.getAllLive()

        return entities.transform {
            it.map { IssueMapper.mapFrom(from = it) }
        }
    }

    override suspend fun loadIssues(): LiveData<List<IssueDO>> {
        val issues = issueDao.getAllLive()
        return issues.transform {
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
        userIssueDao.insert(UserIssueEntity(key = requireNotNull(issueReference.key)))
    }

    override suspend fun downloadImage(name: String): LiveData<ByteArray> {
        val data = MutableLiveData<ByteArray>()
        val task = storageReference.child(name).getBytes(IMG_MAX_SIZE)
        task.addOnSuccessListener {
            data.postValue(it)
        }

        return data
    }

    private fun populateIssueSettings() {
        val settings = IssueType.values().map {
            IssueSettingsEntity(type = it.type, enabled = true)
        }

        issueSettingsDao.insertAll(entities = settings)
    }

    override suspend fun loadUserIssues(): LiveData<List<MyIssueDO>> {
        val data = MutableLiveData<List<MyIssueDO>>()
        val issuesList = mutableListOf<MyIssueDO>()
        val usersIssues = userIssueDao.getAll()
        usersIssues.forEach {
            val key = it.key
            issuesReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val issueSnap = snapshot.child(key)
                    val createTime = issueSnap.child("createTime").value?.toString()?.toLong()
                    val title = issueSnap.child("title").value?.toString()
                    val description = issueSnap.child("description").value?.toString()
                    val imageName = issueSnap.child("img").value?.toString()

                    val task = storageReference.child(requireNotNull(imageName)).getBytes(IMG_MAX_SIZE)
                    task.addOnSuccessListener { bytes ->
                        val issue = MyIssueDO(key, requireNotNull(title), requireNotNull(createTime), requireNotNull(description), bytes)
                        issuesList.add(issue)
                        data.postValue(issuesList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    return
                }
            })

        }

        return data
    }

}