package com.pavelhabzansky.citizenapp.core.vm

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import org.koin.core.KoinComponent

abstract class BaseAndroidViewModel(val app: Application) : AndroidViewModel(app), KoinComponent {

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            app.applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}