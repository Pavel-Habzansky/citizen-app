package com.pavelhabzansky.citizenapp.core.vm

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.pavelhabzansky.domain.features.connectivity.manager.IConnectivityManager
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseAndroidViewModel(val app: Application) : AndroidViewModel(app), KoinComponent {

    val connectivityManager: IConnectivityManager by inject()

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
                app.applicationContext,
                permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission() =
            ContextCompat.checkSelfPermission(
                    app.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                            app.applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED


}