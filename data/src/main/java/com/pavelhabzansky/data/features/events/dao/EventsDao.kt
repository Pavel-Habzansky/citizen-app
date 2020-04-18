package com.pavelhabzansky.data.features.events.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.events.entities.*

@Dao
abstract class EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImage(image: EventImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImages(images: List<EventImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSchedule(schedule: ScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSchedules(schedules: List<ScheduleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocality(locality: LocalityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocalities(localities: List<LocalityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCountry(country: CountryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCountries(countries: List<CountryEntity>)

    @Query("SELECT * FROM EventEntity")
    abstract fun getEvents(): List<EventEntity>

    @Query("SELECT * FROM EventImageEntity WHERE eventId = :id")
    abstract fun getImagesForEvent(id: Int): List<EventImageEntity>

    @Query("SELECT * FROM ScheduleEntity ORDER BY timestampStart ASC")
    abstract fun getSchedules(): List<ScheduleEntity>

    @Query("DELETE FROM ScheduleEntity")
    abstract fun removeSchedules()

    @Query("DELETE FROM EventImageEntity")
    abstract fun removeEventImages()

    @Query("DELETE FROM EventEntity")
    abstract fun removeEvents()

    @Query("DELETE FROM CountryEntity")
    abstract fun removeCountries()

    @Query("DELETE FROM LocalityEntity")
    abstract fun removeLocalities()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPushEvent(entity: PushEventEntity)

    @Query("SELECT * FROM PushEventEntity ORDER BY timestamp DESC")
    abstract fun getPushEvents(): LiveData<List<PushEventEntity>>

    @Query("DELETE FROM PushEventEntity WHERE id = :id")
    abstract fun removePushEvent(id: String)

    @Query("SELECT COUNT(*) FROM PushEventEntity")
    abstract fun inboxSize(): LiveData<Int>


}