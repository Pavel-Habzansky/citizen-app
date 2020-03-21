package com.pavelhabzansky.citizenapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.cities.dao.LastSearchDao
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity
import com.pavelhabzansky.data.features.cities.repository.CityRepository
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitiesRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var lastSearchDao: LastSearchDao
    private lateinit var cityDao: CityDao

    private lateinit var cityRepository: ICityRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        lastSearchDao = database.lastSearchDao
        cityDao = database.cityDao

        cityRepository = CityRepository(
                lastSearchDao = lastSearchDao,
                cityDao = cityDao,
                cityReference = FirebaseDatabase.getInstance().reference,
                storageReference = FirebaseStorage.getInstance().reference,
                newsDao = database.newsDao
        )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun lastSearchInsertSingleTest() {

        val lastSearch = LastSearchCityEntity(key = "ABCDEF", name = "Pilsen", timestamp = 123)

        lastSearchDao.insert(lastSearch)
        val lastSearches = lastSearchDao.getLastSearches()
        Assert.assertEquals(lastSearches.size, 1)
        Assert.assertEquals(lastSearches.first().key, "ABCDEF")
        Assert.assertEquals(lastSearches.first().name, "Pilsen")
        Assert.assertEquals(lastSearches.first().timestamp, 123L)
    }

    @Test
    fun lastSearchInsertMultipleTest() {
        val entities = listOf(
                LastSearchCityEntity(key = "A", name = "Pilsen", timestamp = 123),
                LastSearchCityEntity(key = "B", name = "Prague", timestamp = 123),
                LastSearchCityEntity(key = "C", name = "Brno", timestamp = 123),
                LastSearchCityEntity(key = "D", name = "Ostrava", timestamp = 123)
        )

        lastSearchDao.insertAll(entities)
        val lastSearches = lastSearchDao.getLastSearches()
        Assert.assertNotNull(lastSearches)

        Assert.assertEquals(lastSearches.size, entities.size)
        Assert.assertEquals("Pilsen", lastSearches.find { it.key == "A" }?.name)
        Assert.assertEquals("Prague", lastSearches.find { it.key == "B" }?.name)
        Assert.assertEquals("Brno", lastSearches.find { it.key == "C" }?.name)
        Assert.assertEquals("Ostrava", lastSearches.find { it.key == "D" }?.name)

        Assert.assertNull(lastSearches.find { it.key == "Z" })

        Assert.assertTrue(lastSearches.all { it.timestamp == 123L })
    }

    @Test
    fun saveSearchesTest() {
        val lastSearch = LastSearchItemDO(key = "A", name = "Pilsen")
        runBlocking { cityRepository.saveSearch(search = lastSearch) }
        val inserted = lastSearchDao.getLastSearches()

        Assert.assertEquals(1, inserted.size)
        Assert.assertTrue(inserted.size == 1)
        Assert.assertTrue(inserted.first().key == "A")
        Assert.assertTrue(inserted.first().name == "Pilsen")
    }

    @Test
    fun setResidentialTest() {
        val city = CityInformationDO(
                key = "A",
                id = "ID",
                name = "Pilsen",
                description = "desc",
                population = null,
                rssFeed = "",
                rssUrl = "",
                lat = 0.0,
                lng = 0.0,
                www = ""
        )

        runBlocking { cityRepository.setAsResidential(city) }
        val residential = runBlocking { cityRepository.getResidentialCity() }

        Assert.assertNotNull(residential)

        residential?.let {
            Assert.assertEquals("A", it.key)
            Assert.assertEquals("ID", it.id)
            Assert.assertEquals("Pilsen", it.name)
        }
    }


}