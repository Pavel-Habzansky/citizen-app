package com.pavelhabzansky.citizenapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.repository.IssuesRepository
import com.pavelhabzansky.data.features.settings.dao.IssueSettingsDao
import com.pavelhabzansky.data.features.settings.entities.IssueSettingsEntity
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IssuesRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var issueDao: IssueDao
    private lateinit var issueSettingsDao: IssueSettingsDao

    private lateinit var issuesRepository: IIssuesRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        issueDao = database.issueDao
        issueSettingsDao = database.issueSettingsDao

        issuesRepository = IssuesRepository(
                issuesReference = FirebaseDatabase.getInstance().reference,
                storageReference = FirebaseStorage.getInstance().reference,
                issueDao = issueDao,
                issueSettingsDao = issueSettingsDao
        )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun issueSettingsTest() {
        val settings = IssueType.values().map {
            IssueSettingsEntity(type = it.type, enabled = true)
        }

        issueSettingsDao.insertAll(entities = settings)

        val enabled = issueSettingsDao.getAllEnabled()

        Assert.assertEquals(settings.size, enabled.size)

        Assert.assertTrue(enabled.all { it.enabled })
        Assert.assertTrue(enabled.size == IssueType.values().size)
    }


}