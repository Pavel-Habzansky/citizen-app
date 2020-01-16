package com.pavelhabzansky.citizenapp.core.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.koin.core.KoinComponent

abstract class BaseAndroidViewModel(app: Application) : AndroidViewModel(app), KoinComponent {

}