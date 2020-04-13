package com.pavelhabzansky.data.features.api

data class GoOutResponse(
        val events: Map<Int, Event>,
        val schedules: Map<Int, Schedule>,
        val page: Int,
        val hasNext: Boolean

)