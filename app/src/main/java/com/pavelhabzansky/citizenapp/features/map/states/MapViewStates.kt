package com.pavelhabzansky.citizenapp.features.map.states

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.citizenapp.features.map.view.vo.PlaceVO

sealed class MapViewStates {

    class LocationPermissionGranted : MapViewStates()

    class LocationPermissionNotGranted : MapViewStates()

    class PlacesLoadedEvent(val places: List<PlaceVO>): MapViewStates()

    class IssuesUpdatedEvent(val issues: List<IssueVO>) : MapViewStates()

}

sealed class MapErrorStates(val t: Throwable) {



}