package com.pavelhabzansky.citizenapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.api.PlacesApi
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.entities.PhotoEntity
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.data.features.places.repository.PlacesRepository
import com.pavelhabzansky.data.features.settings.dao.PlaceSettingsDao
import com.pavelhabzansky.data.features.settings.entities.PlaceSettingsEntity
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class PlacesRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var placesDao: PlacesDao
    private lateinit var placeSettingsDao: PlaceSettingsDao

    private lateinit var placesRepository: IPlacesRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        placesDao = database.placesDao
        placeSettingsDao = database.placeSettingDao

        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .client(OkHttpClient().newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        placesRepository = PlacesRepository(
                placesApi = retrofit.create(PlacesApi::class.java),
                placesDao = placesDao,
                placeSettingsDao = placeSettingsDao
        )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun loadAllPlacesTest() {
        val places = runBlocking { placesRepository.loadAllPlaces() }

        Assert.assertNotNull(places)
    }

    @Test
    fun populatePlaceSettingsTest() {
        val method = PlacesRepository::class.java.getDeclaredMethod("populatePlaceSettings")
        method.isAccessible = true

        method.invoke(placesRepository as PlacesRepository)

        val enabled = placeSettingsDao.getAllEnabled()

        Assert.assertNotNull(enabled)

        Assert.assertEquals(PlaceTypeDO.values().size, enabled.size)

        Assert.assertTrue(enabled.all { it.enabled })
    }

    @Test
    fun insertMultipleSettingsTest() {

        val settings = PlaceTypeDO.values().map {
            PlaceSettingsEntity(type = it.type, enabled = true)
        }

        placeSettingsDao.insertAll(settings)

        val count = placeSettingsDao.getCount()
        val enabled = placeSettingsDao.getAllEnabled()

        Assert.assertNotNull(count)
        Assert.assertNotNull(enabled)

        Assert.assertTrue(count == enabled.size)
        Assert.assertTrue(count == PlaceTypeDO.values().size)
        Assert.assertTrue(count == settings.size)


    }

    @Test
    fun setPlaceSettingTest() {
        val settings = PlaceTypeDO.values().map {
            PlaceSettingsEntity(type = it.type, enabled = true)
        }

        placeSettingsDao.insertAll(settings)
        placeSettingsDao.setSetting(listOf(PlaceTypeDO.BAR.type), false)
        val enabled = placeSettingsDao.getAllEnabled()
        val count = placeSettingsDao.getCount()

        Assert.assertFalse(enabled.size == count)

        Assert.assertTrue(enabled.size == settings.size - 1)

        Assert.assertNull(enabled.find { it.type == PlaceTypeDO.BAR.type })
    }

    @Test
    fun photoInsertTest() {
        placesDao.insert(PlaceEntity(
                placeId = "place", location = Gps(0.0, 0.0),
                name = "", rating = null, vicinity = ""
        ))
        val photos = listOf(
                PhotoEntity("ref1", placeId = "place"),
                PhotoEntity("ref2", placeId = "place")
        )

        placesDao.insertPhotos(photos)
        val inserted = placesDao.getPhotos("place")

        Assert.assertNotNull(inserted)

        Assert.assertTrue(inserted.all { it.placeId == "place" })
        Assert.assertTrue(photos.size == inserted.size)

        placesDao.removeAll()
        val after = placesDao.getPhotos("place")

        Assert.assertTrue(after.isEmpty())
    }


}