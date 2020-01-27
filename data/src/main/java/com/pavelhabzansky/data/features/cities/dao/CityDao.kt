package com.pavelhabzansky.data.features.cities.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.cities.entities.CityEntity

@Dao
abstract class CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: CityEntity)

    @Query("UPDATE CityEntity SET residential = 0 WHERE residential = 1")
    abstract fun unsetResidential()

    @Query("SELECT * FROM CityEntity WHERE residential = 1")
    abstract fun getResidential(): CityEntity?

}