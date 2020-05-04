package com.pavelhabzansky.domain.features.issues.domain

data class MyIssueDO(
        val key: String,
        val title: String,
        val date: Long,
        val description: String,
        val image: ByteArray
)