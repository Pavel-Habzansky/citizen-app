package com.pavelhabzansky.citizenapp.features.events.states

import android.graphics.Bitmap

sealed class EventsViewStates {

    class GalleryDownloaded(val gallery: List<Bitmap>) : EventsViewStates()

}

sealed class EventsErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : EventsErrorStates(e)

}