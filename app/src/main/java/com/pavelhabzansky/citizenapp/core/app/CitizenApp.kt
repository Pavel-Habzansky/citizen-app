package com.pavelhabzansky.citizenapp.core.app

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.pavelhabzansky.citizenapp.LogTree
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.config.LogConsumer
import com.pavelhabzansky.citizenapp.core.di.appModule
import com.pavelhabzansky.citizenapp.features.cities.detail.di.cityDetailModule
import com.pavelhabzansky.citizenapp.features.cities.search.di.cityPickerModule
import com.pavelhabzansky.citizenapp.features.issues.create.di.createIssueModule
import com.pavelhabzansky.citizenapp.features.issues.detail.di.issueDetailModule
import com.pavelhabzansky.citizenapp.features.issues.list.di.issueListModule
import com.pavelhabzansky.citizenapp.features.map.di.mapsModule
import com.pavelhabzansky.citizenapp.features.news.di.newsModule
import com.pavelhabzansky.citizenapp.features.place.di.placesModule
import com.pavelhabzansky.citizenapp.features.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CitizenApp : Application() {

    companion object {
        lateinit var instance: CitizenApp
    }

    override fun onCreate() {
        instance = this
        super.onCreate()

        initMisc()
        initKoin()
    }

    private fun initMisc() {
        // Timber
        val logConsumer = LogConsumer(filesDir)
        Timber.plant(LogTree(logConsumer))
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CitizenApp)

            modules(
                    listOf(
                            appModule,
                            newsModule,
                            cityPickerModule,
                            cityDetailModule,
                            mapsModule,
                            createIssueModule,
                            issueDetailModule,
                            issueListModule,
                            placesModule,
                            settingsModule
                    )
            )
        }
    }


}

