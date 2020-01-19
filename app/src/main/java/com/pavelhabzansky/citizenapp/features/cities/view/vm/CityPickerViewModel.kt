package com.pavelhabzansky.citizenapp.features.cities.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.cities.states.CityPickerErrorEvents
import com.pavelhabzansky.citizenapp.features.cities.states.CityPickerViewStates
import com.pavelhabzansky.citizenapp.features.cities.view.mapper.AutoCompleteMapper
import com.pavelhabzansky.citizenapp.features.cities.view.mapper.CityVOMapper
import com.pavelhabzansky.citizenapp.features.cities.view.mapper.LastSearchesVOMapper
import com.pavelhabzansky.citizenapp.features.cities.view.vo.CityVO
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.usecase.LoadFilteredCitiesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.LoadLastSearchesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class CityPickerViewModel : BaseViewModel() {

    private val loadLastSearchesUseCase by inject<LoadLastSearchesUseCase>()
    private val loadFilteredCities by inject<LoadFilteredCitiesUseCase>()

    private var filteredCitiesLiveData: LiveData<List<CityDO>>? = null

    private val filteredCitiesObserver: Observer<List<CityDO>> by lazy {
        Observer<List<CityDO>> {
            viewStateEvent.value = CityPickerViewStates.AutoCompleteLoadedEvent(it.map {
                AutoCompleteMapper.mapFrom(from = it)
            })
        }
    }

    val viewStateEvent = SingleLiveEvent<CityPickerViewStates>()
    val errorViewState = SingleLiveEvent<CityPickerErrorEvents>()

    fun onCityPickerTextUpdate(newText: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            filteredCitiesLiveData = loadFilteredCities(params = newText)
            launch(Dispatchers.Main) { filteredCitiesLiveData?.observeForever(filteredCitiesObserver) }
        }
    }

    fun loadLastUsedSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastSearches = loadLastSearchesUseCase(Unit)

            viewStateEvent.postValue(CityPickerViewStates.LastUsedItemsLoadedEvent(items = lastSearches.map {
                LastSearchesVOMapper.mapFrom(
                    from = it
                )
            }))
        }
    }

}