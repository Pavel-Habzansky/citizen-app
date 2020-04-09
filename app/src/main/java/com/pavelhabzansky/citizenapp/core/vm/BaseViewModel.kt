package com.pavelhabzansky.citizenapp.core.vm

import androidx.lifecycle.ViewModel
import com.pavelhabzansky.domain.features.connectivity.manager.IConnectivityManager
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val connectivityManager: IConnectivityManager by inject()

}