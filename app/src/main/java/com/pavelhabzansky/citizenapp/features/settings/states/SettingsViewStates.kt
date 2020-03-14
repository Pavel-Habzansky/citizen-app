package com.pavelhabzansky.citizenapp.features.settings.states

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceTypeVO

sealed class SettingsViewStates {
    class SettingsLoadedEvent(
            val placeTypes: Set<PlaceTypeVO>,
            val issueTypes: Set<IssueTypeVO>) : SettingsViewStates()

    class PlaceSettingsLoadedEvent(val placeTypes: Set<PlaceTypeVO>) : SettingsViewStates()

    class IssueSettingsLoadedEvent(val issueTypes: Set<IssueTypeVO>) : SettingsViewStates()

    class SettingsSavedEvent : SettingsViewStates()

}

sealed class SettingsErrorEvent(val t: Throwable) {


}