package com.pavelhabzansky.data.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(
    entities = [
    ],
    version = 0
)
abstract class AppDatabase : RoomDatabase() {

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
            )
                .addMigrations()
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}