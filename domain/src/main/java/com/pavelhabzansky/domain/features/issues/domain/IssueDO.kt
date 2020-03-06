package com.pavelhabzansky.domain.features.issues.domain

data class IssueDO(
    val title: String,
    val createTime: Long,
    val type: IssueType,
    val description: String,
    val lat: Double,
    val lng: Double,
    val img: String
)