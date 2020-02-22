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
import com.pavelhabzansky.citizenapp.features.map.states.MapErrorStates
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.domain.features.issues.domain.Bounds
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadLiveIssuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class MapViewModel(val app: Application) : BaseAndroidViewModel(app) {

    private val fetchIssuesUseCase by inject<FetchIssuesUseCase>()
    private val loadLiveIssuesViewModel by inject<LoadLiveIssuesUseCase>()

    private var issuesLiveData: LiveData<List<IssueDO>>? = null

    val mapViewState = SingleLiveEvent<MapViewStates>()
    val mapErrorState = SingleLiveEvent<MapErrorStates>()

    private val issuesObserver: Observer<List<IssueDO>> by lazy {
        Observer<List<IssueDO>> {
            mapViewState.postValue(MapViewStates.IssuesUpdatedEvent(it.map { IssueVOMapper.mapTo(to = it) }))
        }
    }

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

    fun loadIssues(bounds: Bounds) {
        viewModelScope.launch(Dispatchers.IO) {
            issuesLiveData = loadLiveIssuesViewModel(bounds)
            launch(Dispatchers.Main) { issuesLiveData?.observeForever(issuesObserver) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        issuesLiveData?.removeObserver(issuesObserver)
    }

}