package com.pavelhabzansky.citizenapp.features.issues.create.states

import android.graphics.Bitmap

sealed class CreateIssueViewStates {

    class AttachmentSetEvent(val attachment: Bitmap?) : CreateIssueViewStates()

    class CameraPermissionGranted : CreateIssueViewStates()

    class CameraPermissionNotGranted : CreateIssueViewStates()

    class IssueCreated : CreateIssueViewStates()

}

sealed class CreateIssueErrorStates(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : CreateIssueErrorStates(e)

}