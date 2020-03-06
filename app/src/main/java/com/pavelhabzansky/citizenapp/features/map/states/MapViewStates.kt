package com.pavelhabzansky.citizenapp.features.map.states

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

sealed class MapViewStates {

    class LocationPermissionGranted : MapViewStates()

    class LocationPermissionNotGranted : MapViewStates()

    class IssuesUpdatedEvent(val issues: List<IssueVO>) : MapViewStates()

}

sealed class MapErrorStates(val t: Throwable) {



}