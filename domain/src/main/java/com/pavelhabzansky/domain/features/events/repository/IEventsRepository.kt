package com.pavelhabzansky.domain.features.events.repository

import android.graphics.Bitmap
import com.pavelhabzansky.domain.features.events.domain.ScheduleDO

interface IEventsRepository {

    suspend fun downloadEvents(force: Boolean)

    suspend fun loadEvents(): List<ScheduleDO>

    suspend fun downloadImage(src: String): ByteArray

    suspend fun downloadGallery(sources: List<String>): List<ByteArray>

}