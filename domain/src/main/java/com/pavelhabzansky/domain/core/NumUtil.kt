package com.pavelhabzansky.domain.core

fun Double.isBetween(a: Double, b: Double): Boolean {
    return (this in a..b) || (this in b..a) || (this == a && this == b)
}