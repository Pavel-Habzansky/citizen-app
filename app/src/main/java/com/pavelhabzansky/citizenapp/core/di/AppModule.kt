package com.pavelhabzansky.citizenapp.core.di

import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.pavelhabzansky.data.core.room.AppDatabase
import org.koin.dsl.module

val appModule = module {

    single { AppDatabase.getInstance(context = get(), factory = provideSQLiteHelperFactory()) }

}

fun provideSQLiteHelperFactory(): SupportSQLiteOpenHelper.Factory? {
    return SafeHelperFactory("passphrase".toCharArray())
}
