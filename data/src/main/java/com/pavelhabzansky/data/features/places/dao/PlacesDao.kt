package com.pavelhabzansky.data.features.places.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pavelhabzansky.data.features.places.entities.PhotoEntity
import com.pavelhabzansky.data.features.places.entities.PlaceEntity

@Dao
abstract class PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: PlaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<PlaceEntity>)

    @Query("DELETE FROM PlaceEntity")
    abstract fun removeAll()

    @Query("SELECT * FROM PlaceEntity")
    abstract fun getAll(): LiveData<List<PlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM PhotoEntity WHERE placeId = :placeId")
    abstract fun getPhotos(placeId: String): List<PhotoEntity>

}