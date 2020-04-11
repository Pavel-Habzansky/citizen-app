package com.pavelhabzansky.citizenapp.features.issues.list.states

import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItemsVO
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

sealed class MapListViewStates {

    class IssueLoadedEvent(val issues: List<IssueVO>) : MapListViewStates()

    class MapItemsLoaded(val items: MapItemsVO) : MapListViewStates()

}

sealed class MapListErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : MapListErrorStates(e)

}