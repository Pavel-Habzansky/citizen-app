package com.pavelhabzansky.data.features.news.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelhabzansky.data.features.news.entities.NewsEntity

@Dao
abstract class NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(news: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM NewsEntity")
    abstract fun getAll(): LiveData<List<NewsEntity>>

    @Query("SELECT COUNT(*) FROM NewsEntity")
    abstract fun getCount(): Int

}