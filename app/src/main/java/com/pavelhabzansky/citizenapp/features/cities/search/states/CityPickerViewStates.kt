package com.pavelhabzansky.citizenapp.features.cities.search.states

import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.AutoCompleteItem
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.LastCitySearchVO

sealed class CityPickerViewStates {

    class LastUsedItemsLoadedEvent(val items: List<LastCitySearchVO>) : CityPickerViewStates()

    class AutoCompleteLoadedEvent(val items: List<AutoCompleteItem>) : CityPickerViewStates()

}

sealed class CityPickerErrorEvents(val error: Throwable? = null) {

    class UnexpectedErrorEvent(t: Throwable) : CityPickerErrorEvents(t)

}