package com.pavelhabzansky.data.features.issues.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.issues.entities.IssueEntity

@Dao
abstract class IssueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(issue: IssueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(issues: List<IssueEntity>)

    @Query("SELECT * FROM IssueEntity")
    abstract fun getAllLive(): LiveData<List<IssueEntity>>

    @Query("SELECT * FROM IssueEntity")
    abstract fun getAll(): List<IssueEntity>

    @Query("DELETE FROM IssueEntity")
    abstract fun removeAll()

    @Query("SELECT * FROM IssueEntity")
    abstract fun getAllInBounds(): List<IssueEntity>

}