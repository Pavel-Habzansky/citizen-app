package com.pavelhabzansky.citizenapp.features.settings.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceTypeVO
import com.pavelhabzansky.citizenapp.features.settings.states.SettingsErrorEvent
import com.pavelhabzansky.citizenapp.features.settings.states.SettingsViewStates
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO
import com.pavelhabzansky.domain.features.settings.usecase.LoadAllSettingsUseCase
import com.pavelhabzansky.domain.features.settings.usecase.SaveSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel : BaseViewModel() {

    private val loadAllSettingsUseCase by inject<LoadAllSettingsUseCase>()
    private val saveSettingsUseCase by inject<SaveSettingsUseCase>()

    val settingsViewStates = SingleLiveEvent<SettingsViewStates>()
    val settingsErrorStates = SingleLiveEvent<SettingsErrorEvent>()

    private val selectedPlaces = mutableSetOf<PlaceTypeVO>()
    private val selectedIssues = mutableSetOf<IssueTypeVO>()

    fun loadAllSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = loadAllSettingsUseCase(Unit)
            selectedPlaces.addAll(settings.placeSettings.map { PlaceTypeVO.fromDomain(dom = it) })
            selectedIssues.addAll(settings.issueSettings.map { IssueTypeVO.fromString(type = it.type) })

            settingsViewStates.postValue(SettingsViewStates.SettingsLoadedEvent(selectedPlaces, selectedIssues))
        }
    }

    fun saveSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val placesDom = selectedPlaces.map { PlaceTypeDO.fromString(it.type) }.toSet()
            val issuesDom = selectedIssues.map { IssueType.fromString(it.type) }.toSet()

            val settings = SettingsDTO(placesDom, issuesDom)

            saveSettingsUseCase(SaveSettingsUseCase.Params(settings))
            settingsViewStates.postValue(SettingsViewStates.SettingsSavedEvent())
        }
    }

    fun setAllPlaces(enabled: Boolean) {
        if (enabled) {
            selectedPlaces.addAll(PlaceTypeVO.values())
        } else {
            selectedPlaces.clear()
        }

        settingsViewStates.postValue(SettingsViewStates.PlaceSettingsLoadedEvent(selectedPlaces))
    }

    fun setAllIssues(enabled: Boolean) {
        if (enabled) {
            selectedIssues.addAll(IssueTypeVO.values())
        } else {
            selectedIssues.clear()
        }

        settingsViewStates.postValue(SettingsViewStates.IssueSettingsLoadedEvent(selectedIssues))
    }

    fun changePlaceSettings(type: PlaceTypeVO, checked: Boolean) {
        if (checked) {
            selectedPlaces.add(type)
        } else {
            selectedPlaces.remove(type)
        }
    }

    fun changeIssueSettings(type: IssueTypeVO, checked: Boolean) {
        if (checked) {
            selectedIssues.add(type)
        } else {
            selectedIssues.remove(type)
        }
    }

}