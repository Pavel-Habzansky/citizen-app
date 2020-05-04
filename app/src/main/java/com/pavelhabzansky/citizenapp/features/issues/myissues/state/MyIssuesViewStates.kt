package com.pavelhabzansky.citizenapp.features.issues.myissues.state

import com.pavelhabzansky.citizenapp.features.issues.myissues.view.vo.MyIssueVO
import java.lang.Exception

sealed class MyIssuesViewStates {

    class UserIssuesLoadedEvent(val items: List<MyIssueVO>): MyIssuesViewStates()

}

sealed class MyIssuesErrorEvents(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : MyIssuesErrorEvents(e)

}