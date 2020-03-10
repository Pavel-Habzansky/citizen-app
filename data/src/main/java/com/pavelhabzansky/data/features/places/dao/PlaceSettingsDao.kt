package com.pavelhabzansky.data.features.places.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.places.entities.PlaceSettingsEntity

@Dao
abstract class PlaceSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<PlaceSettingsEntity>)

    @Query("SELECT * FROM PlaceSettingsEntity WHERE enabled = 1")
    abstract fun getAllEnabled(): List<PlaceSettingsEntity>

    @Query("UPDATE PlaceSettingsEntity SET enabled = :enabled WHERE type IN(:types)")
    abstract fun setSetting(types: List<String>, enabled: Boolean)

    @Query("SELECT COUNT(*) FROM PlaceSettingsEntity")
    abstract fun getCount(): Int

}