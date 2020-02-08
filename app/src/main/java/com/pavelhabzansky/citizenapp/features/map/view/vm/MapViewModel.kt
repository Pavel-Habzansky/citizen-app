package com.pavelhabzansky.citizenapp.features.map.view.vm

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.features.map.states.MapErrorStates
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates

class MapViewModel(val app: Application) : BaseAndroidViewModel(app) {

    val mapViewState = SingleLiveEvent<MapViewStates>()
    val mapErrorState = SingleLiveEvent<MapErrorStates>()

    fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                app.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapViewState.postValue(MapViewStates.LocationPermissionGranted())
        } else {
            mapViewState.postValue(MapViewStates.LocationPermissionNotGranted())
        }
    }

}