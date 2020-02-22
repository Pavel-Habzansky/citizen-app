package com.pavelhabzansky.citizenapp.features.map.view.vo

data class IssueVO(
    val title: String,
    val createTime: Long,
    val type: IssueTypeVO,
    val description: String,
    val lat: Double,
    val lng: Double,
    val img: String
)