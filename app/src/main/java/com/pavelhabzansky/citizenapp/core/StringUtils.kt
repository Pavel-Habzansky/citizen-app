package com.pavelhabzansky.citizenapp.core

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val fromRssFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)

fun String.fromRssDate(): Date? {
    return try {
        fromRssFormat.parse(this)
    } catch (e: ParseException) {
        Timber.i("Unparsable date: $this")
        null
    }
}