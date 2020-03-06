package com.pavelhabzansky.data.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, K> LiveData<T>.transform(mapping: (T) -> K): LiveData<K> {
    return Transformations.map(this) {
        mapping(it)
    }
}