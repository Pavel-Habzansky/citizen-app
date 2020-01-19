package com.pavelhabzansky.citizenapp.core.di

import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.cities.repository.CityRepository
import com.pavelhabzansky.data.features.news.repository.NewsRepository
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val QUAL_FIREBASE_ROOT = "FIREBASE_ROOT"
const val QUAL_FIREBASE_CITIES = "FIREBASE_CITIES"
const val QUAL_FIREBASE_INCIDENTS = "FIREBASE_INCIDENTS"

val appModule = module {

    single(qualifier = named(QUAL_FIREBASE_ROOT)) { provideFirebaseReference() }

    single(qualifier = named(QUAL_FIREBASE_CITIES)) { provideFirebaseReference(path = "cities") }

    single(qualifier = named(QUAL_FIREBASE_INCIDENTS)) { provideFirebaseReference(path = "incidents") }

    single { AppDatabase.getInstance(context = get(), factory = provideSQLiteHelperFactory()) }

    single {
        CityRepository(
            cityReference = get(named(QUAL_FIREBASE_CITIES)),
            lastSearchDao = get<AppDatabase>().lastSearchDao
        ) as ICityRepository
    }

    single {
        NewsRepository(
            citiesReference = get(named(QUAL_FIREBASE_CITIES))
        ) as INewsRepository
    }

}

fun provideFirebaseReference(path: String? = null): DatabaseReference {
    val reference = FirebaseDatabase.getInstance().reference
    path?.let {
        return reference.child(it)
    }
    return reference
}

fun provideSQLiteHelperFactory(): SupportSQLiteOpenHelper.Factory? {
    return SafeHelperFactory("passphrase".toCharArray())
}
