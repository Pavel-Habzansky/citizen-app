package com.pavelhabzansky.data.features.events.repository

import com.pavelhabzansky.data.features.api.GoOutApi
import com.pavelhabzansky.data.features.events.dao.EventSettingDao
import com.pavelhabzansky.data.features.events.dao.EventsDao
import com.pavelhabzansky.data.features.events.entities.*
import com.pavelhabzansky.data.features.events.mapper.*
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO
import com.pavelhabzansky.domain.features.events.domain.ScheduleDO
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.URL
import java.sql.Timestamp

class EventsRepository(
        private val eventsDao: EventsDao,
        private val eventSettingDao: EventSettingDao
) : IEventsRepository {

    override suspend fun downloadEvents(force: Boolean) {
        if (eventsDao.getEvents().isNotEmpty()) {
            return
        }

        val retrofit = Retrofit.Builder()
                .baseUrl("https://goout.net/services/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val goOutApi = retrofit.create(GoOutApi::class.java)

        val countries = mutableSetOf<CountryEntity>()
        val localities = mutableSetOf<LocalityEntity>()
        val images = mutableListOf<EventImageEntity>()
        val events = mutableSetOf<EventEntity>()
        val schedules = mutableSetOf<ScheduleEntity>()

        val currentTime = System.currentTimeMillis()
        var pageCounter = 1
        do {
            val call = goOutApi.fetchEvents(page = pageCounter)
            pageCounter++
            val response = call.execute()
            val body = response.body()

            if (!response.isSuccessful) {
                continue
            }

            body?.let {
                val schedulesBody = it.schedules.values.filter { Timestamp.valueOf(it.start).time > currentTime }
                val eventsBody = it.events.values.filter { schedulesBody.map { it.eventId }.contains(it.id) }
                localities.addAll(schedulesBody.map { LocalityMapper.mapFrom(it.venueLocality) })
                countries.addAll(schedulesBody.map { it.venueLocality }.map { CountryMapper.mapFrom(it.country) })
                schedules.addAll(schedulesBody.map { ScheduleMapper.mapFrom(it) })
                images.addAll(eventsBody.flatMap { event -> event.images.map { EventImageMapper.mapFrom(it).also { it.eventId = event.id } } })
                events.addAll(eventsBody.map { EventMapper.mapFrom(it) })
            }

        } while (body?.hasNext == true && pageCounter <= 5)

        eventsDao.removeSchedules()
        eventsDao.removeEventImages()
        eventsDao.removeEvents()
        eventsDao.removeLocalities()
        eventsDao.removeCountries()

        eventsDao.insertCountries(countries.toList())
        eventsDao.insertLocalities(localities.toList())
        eventsDao.insertEvents(events.toList())
        eventsDao.insertImages(images)
        eventsDao.insertSchedules(schedules.toList())

        val newSettings = localities.map {
            CitySettingEntity(enumName = it.enum, name = it.name, countryCode = it.countryCode)
        }
        val currentSettings = eventSettingDao.getCitySettings()

        val diff = newSettings.minus(currentSettings)
        eventSettingDao.insertCitySettings(diff)
    }

    override suspend fun loadEvents(): List<ScheduleDO> {
        val imageMap = mutableMapOf<Int, List<EventImageEntity>>()
        val events = eventsDao.getEvents()
        var schedules = eventsDao.getSchedules()

        val cityEnums = eventSettingDao.getCitySettings().filter { it.enabled }.map { it.enumName }
        schedules = schedules.filter { cityEnums.contains(it.localityEnum) }

        schedules.forEach {
            imageMap[it.id] = eventsDao.getImagesForEvent(it.eventId)
        }

        return schedules.map { schedule ->
            val event = events.find { it.id == schedule.eventId }!!
            ScheduleDO(id = schedule.id, name = event.name,
                    text = event.text, mainImageUrl = event.mainImageSrc,
                    images = imageMap[schedule.id]?.map { it.src } ?: emptyList(),
                    date = schedule.start, url = schedule.url, image = downloadImage(event.mainImageSrc),
                    pricing = schedule.pricing, currency = schedule.currency, locality = schedule.locality)
        }
    }

    override suspend fun downloadImage(src: String): ByteArray {
        return try {
            val connection = URL(src).openConnection()
            connection.doInput = true
            connection.connect()
            val inStream = connection.getInputStream()
            inStream.readBytes()
        } catch (e: Exception) {
            ByteArray(0)
        }
    }

    override suspend fun downloadGallery(sources: List<String>): List<ByteArray> {
        return sources.map { src ->
            try {
                val connection = URL(src).openConnection()
                connection.doInput = true
                connection.connect()
                val inStream = connection.getInputStream()
                inStream.readBytes()
            } catch (e: Exception) {
                ByteArray(0)
            }
        }
    }

    override suspend fun getFilterSettings(): List<CitySettingDO> {
        val settings = eventSettingDao.getCitySettings()
        return settings.map { CitySettingMapper.mapFrom(it) }.sortedWith(compareBy({ it.countryCode }, { it.name }))
    }

    override suspend fun saveFilter(settings: List<CitySettingDO>) {
        eventSettingDao.insertCitySettings(settings.map { CitySettingMapper.mapTo(it) })
    }

}