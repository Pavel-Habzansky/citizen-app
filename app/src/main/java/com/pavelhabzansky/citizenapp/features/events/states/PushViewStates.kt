package com.pavelhabzansky.citizenapp.features.events.states

import com.pavelhabzansky.citizenapp.features.events.view.vo.PushEventVO
import java.lang.Exception

sealed class PushViewStates {

    class PushEventsLoaded(val events: List<PushEventVO>) : PushViewStates()

}

sealed class PushErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : PushErrorStates(e)

}