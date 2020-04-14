package com.pavelhabzansky.citizenapp.features.filter.states

import com.pavelhabzansky.citizenapp.features.filter.view.vo.CitySettingVO

sealed class FilterViewState {

    class CitySettingsLoadedEvent(val settings: List<CitySettingVO>) : FilterViewState()

    class FilterSavedEvent : FilterViewState()

}