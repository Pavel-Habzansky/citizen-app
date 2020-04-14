package com.pavelhabzansky.citizenapp.features.filter.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.filter.states.FilterViewState
import com.pavelhabzansky.citizenapp.features.filter.view.mapper.CitySettingVOMapper
import com.pavelhabzansky.citizenapp.features.filter.view.vo.CitySettingVO
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO
import com.pavelhabzansky.domain.features.events.usecase.GetSettingsUseCase
import com.pavelhabzansky.domain.features.events.usecase.SaveFilterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class FilterViewModel : BaseViewModel() {

    private val getSettings by inject<GetSettingsUseCase>()
    private val saveSettings by inject<SaveFilterUseCase>()

    val filterViewState = SingleLiveEvent<FilterViewState>()

    private var currentSettings: MutableList<CitySettingVO> = mutableListOf()

    fun loadFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = getSettings(Unit)
            currentSettings = settings.map { CitySettingVOMapper.mapFrom(it) }.toMutableList()
            filterViewState.postValue(FilterViewState.CitySettingsLoadedEvent(currentSettings))
        }
    }

    fun onCitySettingCheck(item: CitySettingVO) {
        currentSettings.find { it.enumName == item.enumName }?.enabled = item.enabled
        filterViewState.value = FilterViewState.CitySettingsLoadedEvent(currentSettings)
    }

    fun setAll(enabled: Boolean) {
        currentSettings.onEach { it.enabled = enabled }
        filterViewState.value = FilterViewState.CitySettingsLoadedEvent(currentSettings)
    }

    fun saveCurrent() {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettings(currentSettings.map { CitySettingDO(it.enumName, it.name, it.enabled) })
            filterViewState.postValue(FilterViewState.FilterSavedEvent())
        }
    }

}