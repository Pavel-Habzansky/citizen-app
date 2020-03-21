package com.pavelhabzansky.citizenapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.database.FirebaseDatabase
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.news.dao.NewsDao
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.data.features.news.repository.NewsRepository
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var cityDao: CityDao
    private lateinit var newsDao: NewsDao

    private lateinit var newsRepository: INewsRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        cityDao = database.cityDao
        newsDao = database.newsDao

        newsRepository = NewsRepository(
                cityDao = cityDao,
                newsDao = newsDao,
                cityReference = FirebaseDatabase.getInstance().reference
        )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun loadCachedNewsTest() {
        val news = runBlocking { newsRepository.loadCachedNews() }

        Assert.assertNotNull(news)
    }

    @Test
    fun newsInsertTest() {
        val newsEntity = NewsEntity(title = "title", description = "description", url = "url", date = "date", read = false)

        newsDao.insert(newsEntity)
        val inserted = newsDao.getAll()

        Assert.assertNotNull(inserted)

        Assert.assertEquals(1, inserted.size)
        Assert.assertEquals(newsEntity.title, inserted.first().title)
        Assert.assertEquals(newsEntity.description, inserted.first().description)
        Assert.assertEquals(newsEntity.url, inserted.first().url)
        Assert.assertEquals(newsEntity.date, inserted.first().date)
        Assert.assertEquals(newsEntity.read, inserted.first().read)

        newsDao.removeAll()
        val after = newsDao.getAll()

        Assert.assertEquals(0, after.size)
    }

    @Test
    fun newsInsertAllTest() {
        val entities = listOf(
                NewsEntity(title = "1", description = "desc1", url = "url", date = "date", read = false),
                NewsEntity(title = "2", description = "desc2", url = "url", date = "date", read = false),
                NewsEntity(title = "3", description = "desc3", url = "url", date = "date", read = false)
        )

        newsDao.insertAll(entities)
        val inserted = newsDao.getAll()

        Assert.assertNotNull(inserted)

        Assert.assertEquals(entities.size, inserted.size)
        Assert.assertEquals("desc1", inserted.find { it.title == "1" }?.description)
        Assert.assertEquals("desc2", inserted.find { it.title == "2" }?.description)
        Assert.assertEquals("desc3", inserted.find { it.title == "3" }?.description)

        Assert.assertTrue(inserted.all { !it.read })

        newsDao.removeAll()
        val after = newsDao.getAll()

        Assert.assertEquals(0, after.size)
    }


}