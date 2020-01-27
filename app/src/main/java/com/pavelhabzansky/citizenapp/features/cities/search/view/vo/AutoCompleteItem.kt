package com.pavelhabzansky.citizenapp.features.cities.search.view.vo

data class AutoCompleteItem(
    val key: String,
    val name: String
) {

    override fun toString(): String {
        return name
    }

}