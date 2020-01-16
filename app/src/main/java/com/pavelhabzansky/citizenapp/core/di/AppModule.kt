package com.pavelhabzansky.citizenapp.core.di

import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pavelhabzansky.data.core.room.AppDatabase
import com.pavelhabzansky.data.features.news.repository.NewsRepository
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val QUAL_FIREBASE_ROOT = "FIREBASE_ROOT"

val appModule = module {

    single(qualifier = named(QUAL_FIREBASE_ROOT)) { provideFirebaseReference() }

    single { AppDatabase.getInstance(context = get(), factory = provideSQLiteHelperFactory()) }

    single {
        NewsRepository(

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
