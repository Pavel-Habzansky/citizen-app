package com.pavelhabzansky.data.features.cities.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity

@Dao
abstract class LastSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: LastSearchCityEntity)

    @Query("SELECT * FROM LastSearchCityEntity ORDER BY timestamp ASC")
    abstract fun getLastSearches(): LiveData<List<LastSearchCityEntity>>

}