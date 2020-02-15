package com.pavelhabzansky.citizenapp.core

import com.google.gson.Gson

val gson = Gson()

fun Any.toJson(): String {
    return gson.toJson(this)
}

fun <T> String.fromJson(clazz: Class<T>): T {
    return gson.fromJson(this, clazz)
}
