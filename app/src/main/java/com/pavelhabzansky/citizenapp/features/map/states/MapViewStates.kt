package com.pavelhabzansky.citizenapp.features.map.states

sealed class MapViewStates {

    class LocationPermissionGranted : MapViewStates()

    class LocationPermissionNotGranted : MapViewStates()

}

sealed class MapErrorStates(val t: Throwable) {



}