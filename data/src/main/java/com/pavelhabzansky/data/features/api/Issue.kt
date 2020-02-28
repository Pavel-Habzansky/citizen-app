package com.pavelhabzansky.data.features.api

import com.pavelhabzansky.data.features.issues.model.Gps

data class Issue(
    var createTime: Long = 0,
    var description: String = "UNKNOWN",
    var gps: Gps = Gps(0.0, 0.0),
    var img: String = "UNKNOWN",
    var time: Long = 0,
    var title: String = "UNKNOWN",
    var type: String = "UNKNOWN"
)