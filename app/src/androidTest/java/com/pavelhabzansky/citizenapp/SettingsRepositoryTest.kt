package com.pavelhabzansky.citizenapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.settings.dao.IssueSettingsDao
import com.pavelhabzansky.data.features.settings.dao.PlaceSettingsDao
import com.pavelhabzansky.data.features.settings.entities.IssueSettingsEntity
import com.pavelhabzansky.data.features.settings.entities.PlaceSettingsEntity
import com.pavelhabzansky.data.features.settings.repository.SettingsRepository
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO
import com.pavelhabzansky.domain.features.settings.repository.ISettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var placeSettingsDao: PlaceSettingsDao
    private lateinit var issueSettingsDao: IssueSettingsDao

    private lateinit var settingsRepository: ISettingsRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        placeSettingsDao = database.placeSettingDao
        issueSettingsDao = database.issueSettingsDao

        settingsRepository = SettingsRepository(
                placeSettingsDao,
                issueSettingsDao
        )

        populateSettings()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun loadAllSettingsTest() {
        val settings = runBlocking { settingsRepository.loadAllSettings() }

        Assert.assertNotNull(settings)
        Assert.assertNotNull(settings.placeSettings)
        Assert.assertNotNull(settings.issueSettings)

        Assert.assertTrue(settings.placeSettings.size == PlaceTypeDO.values().size)
        Assert.assertTrue(settings.issueSettings.size == IssueType.values().size)
    }

    @Test
    fun saveSettingsTest() {
        val placeSettings = setOf(PlaceTypeDO.BAR)
        val issueSettings = setOf(IssueType.TRASH)

        val settings = SettingsDTO(placeSettings, issueSettings)

        runBlocking { settingsRepository.saveSettings(settings) }
        val after = runBlocking { settingsRepository.loadAllSettings() }

        Assert.assertNotNull(after)
        Assert.assertNotNull(after.placeSettings)
        Assert.assertNotNull(after.issueSettings)

        Assert.assertTrue(after.issueSettings.size == settings.issueSettings.size)
        Assert.assertTrue(after.placeSettings.size == settings.placeSettings.size)
    }

    private fun populateSettings() {
        populateIssueSettings()
        populatePlaceSettings()
    }

    private fun populateIssueSettings() {
        val settings = IssueType.values().map {
            IssueSettingsEntity(type = it.type, enabled = true)
        }

        issueSettingsDao.insertAll(entities = settings)
    }

    private fun populatePlaceSettings() {
        val settings = PlaceTypeDO.values().map {
            PlaceSettingsEntity(type = it.type, enabled = true)
        }

        placeSettingsDao.insertAll(entities = settings)
    }

}