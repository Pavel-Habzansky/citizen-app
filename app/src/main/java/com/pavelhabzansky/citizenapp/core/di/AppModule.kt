package com.pavelhabzansky.citizenapp.core.di

import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.cities.repository.CityRepository
import com.pavelhabzansky.data.features.issues.repository.IssuesRepository
import com.pavelhabzansky.data.features.news.repository.NewsRepository
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val QUAL_FIREBASE_ROOT = "FIREBASE_ROOT"
const val QUAL_FIREBASE_CITIES = "FIREBASE_CITIES"
const val QUAL_FIREBASE_INCIDENTS = "FIREBASE_INCIDENTS"

val appModule = module {

    single(qualifier = named(QUAL_FIREBASE_ROOT)) { provideFirebaseReference() }

    single(qualifier = named(QUAL_FIREBASE_CITIES)) { provideFirebaseReference(path = "cities") }

    single(qualifier = named(QUAL_FIREBASE_INCIDENTS)) { provideFirebaseReference(path = "incidents") }

    single { provideFirebaseStorageReference() }

    single { AppDatabase.getInstance(context = get(), factory = provideSQLiteHelperFactory()) }

    single {
        IssuesRepository(
            issuesReference = get(named(QUAL_FIREBASE_INCIDENTS)),
            storageReference = get(),
            issueDao = get<AppDatabase>().issueDao
        ) as IIssuesRepository
    }

    single {
        CityRepository(
            cityReference = get(named(QUAL_FIREBASE_CITIES)),
            storageReference = get(),
            lastSearchDao = get<AppDatabase>().lastSearchDao,
            newsDao = get<AppDatabase>().newsDao,
            cityDao = get<AppDatabase>().cityDao
        ) as ICityRepository
    }

    single {
        NewsRepository(
            cityDao = get<AppDatabase>().cityDao,
            newsDao = get<AppDatabase>().newsDao
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

fun provideFirebaseStorageReference(): StorageReference {
    return FirebaseStorage.getInstance().reference
}

fun provideSQLiteHelperFactory(): SupportSQLiteOpenHelper.Factory? {
    return SafeHelperFactory("passphrase".toCharArray())
}