package com.pavelhabzansky.data.features.settings.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.settings.entities.IssueSettingsEntity

@Dao
abstract class IssueSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<IssueSettingsEntity>)

    @Query("SELECT * FROM IssueSettingsEntity WHERE enabled = 1")
    abstract fun getAllEnabled(): List<IssueSettingsEntity>

    @Query("UPDATE IssueSettingsEntity SET enabled = :enabled WHERE type IN(:types)")
    abstract fun setSetting(types: List<String>, enabled: Boolean)

    @Query("SELECT COUNT(*) FROM IssueSettingsEntity")
    abstract fun getCount(): Int

}