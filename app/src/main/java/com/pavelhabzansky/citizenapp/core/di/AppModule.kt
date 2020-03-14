package com.pavelhabzansky.citizenapp.core.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.google.android.libraries.places.api.Places
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.api.PlacesApi
import com.pavelhabzansky.data.features.cities.repository.CityRepository
import com.pavelhabzansky.data.features.issues.repository.IssuesRepository
import com.pavelhabzansky.data.features.news.repository.NewsRepository
import com.pavelhabzansky.data.features.places.repository.PlacesRepository
import com.pavelhabzansky.data.features.settings.repository.SettingsRepository
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import com.pavelhabzansky.domain.features.issues.repository.IIssuesRepository
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository
import com.pavelhabzansky.domain.features.settings.repository.ISettingsRepository
import okhttp3.OkHttpClient
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val QUAL_FIREBASE_ROOT = "FIREBASE_ROOT"
const val QUAL_FIREBASE_CITIES = "FIREBASE_CITIES"
const val QUAL_FIREBASE_INCIDENTS = "FIREBASE_INCIDENTS"

val appModule = module {

    single(qualifier = named(QUAL_FIREBASE_ROOT)) { provideFirebaseReference() }

    single(qualifier = named(QUAL_FIREBASE_CITIES)) { provideFirebaseReference(path = "cities") }

    single(qualifier = named(QUAL_FIREBASE_INCIDENTS)) { provideFirebaseReference(path = "incidents") }

    single { providePlacesApi() }

    single { provideFirebaseStorageReference() }

    single { AppDatabase.getInstance(context = get(), factory = provideSQLiteHelperFactory()) }

    single {
        IssuesRepository(
                issuesReference = get(named(QUAL_FIREBASE_INCIDENTS)),
                storageReference = get(),
                issueDao = get<AppDatabase>().issueDao,
                issueSettingsDao = get<AppDatabase>().issueSettingsDao
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

    single {
        PlacesRepository(
                placesApi = get(),
                placesDao = get<AppDatabase>().placesDao,
                placeSettingsDao = get<AppDatabase>().placeSettingDao
        ) as IPlacesRepository
    }

    single {
        SettingsRepository(
                placeSettingsDao = get<AppDatabase>().placeSettingDao,
                issueSettingsDao = get<AppDatabase>().issueSettingsDao
        ) as ISettingsRepository
    }

}

fun providePlacesApi(): PlacesApi {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .client(OkHttpClient().newBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    return retrofit.create(PlacesApi::class.java)
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