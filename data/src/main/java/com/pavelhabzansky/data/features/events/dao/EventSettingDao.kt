package com.pavelhabzansky.data.features.events.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.events.entities.CitySettingEntity

@Dao
abstract class EventSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCitySettings(settings: List<CitySettingEntity>)

    @Query("SELECT * FROM CitySettingEntity")
    abstract fun getCitySettings(): List<CitySettingEntity>

}