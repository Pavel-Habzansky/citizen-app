package com.pavelhabzansky.data.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.pavelhabzansky.data.core.Converters
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.cities.dao.LastSearchDao
import com.pavelhabzansky.data.features.cities.entities.CityEntity
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.data.features.news.dao.NewsDao
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.entities.PlaceEntity

@Database(
        entities = [
            LastSearchCityEntity::class,
            CityEntity::class,
            NewsEntity::class,
            IssueEntity::class,
            PlaceEntity::class
        ],
        version = 5
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val cityDao: CityDao
    abstract val newsDao: NewsDao
    abstract val lastSearchDao: LastSearchDao
    abstract val issueDao: IssueDao
    abstract val placesDao: PlacesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "citizen-app.db"
        const val DB_KEY_NAME = "DB_KEY"

        fun getInstance(context: Context, factory: SupportSQLiteOpenHelper.Factory?): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context, factory).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(
                context: Context,
                factory: SupportSQLiteOpenHelper.Factory? = null
        ): AppDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DB_NAME
            ).addMigrations()
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }


}