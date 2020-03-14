package com.pavelhabzansky.citizenapp.features.place.states

import android.graphics.Bitmap

sealed class PlaceDetailViewStates {

    class GalleryLoadedState(val gallery: List<Bitmap>) : PlaceDetailViewStates()

}

sealed class PlaceDetailErrorStates(val t: Throwable) {


}