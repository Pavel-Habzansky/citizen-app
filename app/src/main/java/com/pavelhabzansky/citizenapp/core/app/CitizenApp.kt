package com.pavelhabzansky.citizenapp.core.app

import android.app.Application
import com.pavelhabzansky.citizenapp.LogTree
import com.pavelhabzansky.citizenapp.core.config.LogConsumer
import com.pavelhabzansky.citizenapp.core.di.appModule
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
                    appModule
                )
            )
        }
    }


}

