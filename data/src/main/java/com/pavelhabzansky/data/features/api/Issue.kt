package com.pavelhabzansky.data.features.api

import com.pavelhabzansky.data.features.issues.model.Gps

data class Issue(
    var title: String = "UNKNOWN",
    var createTime: Long = 0,
    var type: String = "UNKNOWN",
    var description: String = "UNKNOWN",
    var gps: Gps = Gps(0.0, 0.0),
    var img: String = "UNKNOWN"
)