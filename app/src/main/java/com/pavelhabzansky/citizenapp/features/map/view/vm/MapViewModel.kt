package com.pavelhabzansky.citizenapp.features.map.view.vm

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.features.map.di.MAPS_MODULE
import com.pavelhabzansky.citizenapp.features.map.states.MapErrorStates
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.citizenapp.features.map.view.mapper.PlacesVOMapper
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadBoundIssuesUseCase
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase
import com.pavelhabzansky.domain.features.places.usecase.LoadPlacesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import org.koin.core.qualifier.named

class MapViewModel(app: Application) : BaseAndroidViewModel(app) {

    private val fetchIssuesUseCase by inject<FetchIssuesUseCase>(qualifier = named(MAPS_MODULE))
    private val fetchPlacesUseCase by inject<FetchPlacesUseCase>()
    private val loadPlacesUseCase by inject<LoadPlacesUseCase>()
    private val loadBoundIssuesUseCase by inject<LoadBoundIssuesUseCase>()

    val mapViewState = SingleLiveEvent<MapViewStates>()
    val mapErrorState = SingleLiveEvent<MapErrorStates>()

    private val placesObserver: Observer<List<PlaceDO>> by lazy {
        Observer<List<PlaceDO>> { places ->
            val placesVOList = places.map { PlacesVOMapper.mapFrom(from = it) }
            mapViewState.postValue(MapViewStates.PlacesLoadedEvent(placesVOList))
        }
    }

    private var placesLiveData: LiveData<List<PlaceDO>>? = null

    fun requestLocationPermission() {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
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

    fun fetchPlaces(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPlacesUseCase(FetchPlacesUseCase.Params(lat, lng))
        }
    }

    fun loadIssues(bounds: Bounds) {
        viewModelScope.launch(Dispatchers.IO) {
            val issues = loadBoundIssuesUseCase(bounds)
            mapViewState.postValue(MapViewStates.IssuesUpdatedEvent(issues.map {
                IssueVOMapper.mapTo(
                        to = it
                )
            }))
        }
    }

    fun loadPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            placesLiveData = loadPlacesUseCase(Unit)
            launch(Dispatchers.Main) { placesLiveData?.observeForever(placesObserver) }
        }
    }

}