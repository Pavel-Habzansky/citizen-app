package com.pavelhabzansky.citizenapp.features.issues.detail.states

import android.graphics.Bitmap
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO

sealed class IssueDetailViewStates {

    class IssueDetailLoadedEvent(val issue: IssueVO) : IssueDetailViewStates()

    class IssueImageLoadedEvent(val image: Bitmap) : IssueDetailViewStates()

}

sealed class IssueDetailErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val exception: Exception) : IssueDetailErrorStates(exception)

}