package com.pavelhabzansky.data.features.cities.dao

import androidx.room.Dao
import androidx.room.Query
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity

@Dao
abstract class LastSearchDao {

    @Query("SELECT * FROM LastSearchCityEntity ORDER BY timestamp ASC")
    abstract fun getLastSearches(): List<LastSearchCityEntity>

}