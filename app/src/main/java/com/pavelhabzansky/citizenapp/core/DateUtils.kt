package com.pavelhabzansky.citizenapp.core

import java.text.SimpleDateFormat
import java.util.*

val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.ENGLISH)

fun Date.toFormattedString(): String {
    return formatter.format(this)
}

fun Long.timestampToString(): String {
    return formatter.format(Date(this))
}