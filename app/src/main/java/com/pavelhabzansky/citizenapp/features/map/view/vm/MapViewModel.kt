package com.pavelhabzansky.citizenapp.features.map.view.vm

import android.Manifest
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.USE_CONTEXT_CITIZEN
import com.pavelhabzansky.citizenapp.core.USE_CONTEXT_EMPTY
import com.pavelhabzansky.citizenapp.core.USE_CONTEXT_TOURIST
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.features.map.di.MAPS_MODULE
import com.pavelhabzansky.citizenapp.features.map.states.MapErrorStates
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.citizenapp.features.place.view.mapper.PlacesVOMapper
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

    private val issuesObserver: Observer<List<IssueDO>> by lazy {
        Observer<List<IssueDO>> {
            val issuesVOList = it.map { IssueVOMapper.mapTo(to = it) }
            mapViewState.postValue(MapViewStates.IssuesUpdatedEvent(issuesVOList))
        }
    }

    private var placesLiveData: LiveData<List<PlaceDO>>? = null
    private var issuesLiveData: LiveData<List<IssueDO>>? = null

    var useContext: String = USE_CONTEXT_EMPTY

    fun loadData() {
        when (useContext) {
            USE_CONTEXT_CITIZEN, USE_CONTEXT_EMPTY -> {
                fetchIssues()
                loadIssues()
            }
        }
    }

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
        if (useContext == USE_CONTEXT_TOURIST || useContext == USE_CONTEXT_EMPTY) {
            viewModelScope.launch(Dispatchers.IO) {
                when (connectivityManager.isConnected()) {
                    true -> fetchPlacesUseCase(FetchPlacesUseCase.Params(lat, lng))
                    else -> mapViewState.postValue(MapViewStates.PlacesNoConnectionEvent())
                }
            }
        }
    }

    fun loadIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            when (useContext) {
                USE_CONTEXT_CITIZEN, USE_CONTEXT_EMPTY -> {
                    issuesLiveData = loadBoundIssuesUseCase(Unit)
                    launch(Dispatchers.Main) { issuesLiveData?.observeForever(issuesObserver) }
                }
            }
        }
    }

    fun loadPlaces() {
        if (useContext == USE_CONTEXT_TOURIST || useContext == USE_CONTEXT_EMPTY) {
            viewModelScope.launch(Dispatchers.IO) {
                placesLiveData = loadPlacesUseCase(Unit)
                launch(Dispatchers.Main) { placesLiveData?.observeForever(placesObserver) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        issuesLiveData?.removeObserver(issuesObserver)
        placesLiveData?.removeObserver(placesObserver)
    }

}