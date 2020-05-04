package com.pavelhabzansky.data.features.issues.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.issues.entities.UserIssueEntity

@Dao
abstract class UserIssueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: UserIssueEntity)

    @Query("SELECT * FROM UserIssueEntity")
    abstract fun getAll(): List<UserIssueEntity>

}