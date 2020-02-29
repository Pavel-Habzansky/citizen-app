package com.pavelhabzansky.citizenapp.features.issues.list.states

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

sealed class IssueListViewStates {

class IssueLoadedEvent(val issues: List<IssueVO>) : IssueListViewStates()

}

sealed class IssueListErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : IssueListErrorStates(e)

}