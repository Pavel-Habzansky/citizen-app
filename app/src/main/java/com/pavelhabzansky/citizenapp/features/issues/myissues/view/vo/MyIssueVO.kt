package com.pavelhabzansky.citizenapp.features.issues.myissues.view.vo

import android.graphics.Bitmap

data class MyIssueVO(
        val key: String,
        val title: String,
        val date: Long,
        val description: String,
        var image: Bitmap? = null
)