package com.pavelhabzansky.citizenapp.features.map.view.vm

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.features.map.states.MapErrorStates
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class MapViewModel(val app: Application) : BaseAndroidViewModel(app) {

    private val fetchIssuesUseCase by inject<FetchIssuesUseCase>()

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

    fun fetchIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchIssuesUseCase(Unit)
        }
    }

    fun loadIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO
        }
    }

}