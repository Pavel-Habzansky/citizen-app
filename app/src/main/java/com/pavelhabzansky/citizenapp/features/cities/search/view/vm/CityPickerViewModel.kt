package com.pavelhabzansky.citizenapp.features.cities.search.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.cities.search.states.CityPickerErrorEvents
import com.pavelhabzansky.citizenapp.features.cities.search.states.CityPickerViewStates
import com.pavelhabzansky.citizenapp.features.cities.search.view.mapper.AutoCompleteMapper
import com.pavelhabzansky.citizenapp.features.cities.search.view.mapper.CityVOMapper
import com.pavelhabzansky.citizenapp.features.cities.search.view.mapper.LastSearchesVOMapper
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.AutoCompleteItem
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.usecase.LoadFilteredCitiesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.LoadLastSearchesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.SaveSearchedItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class CityPickerViewModel : BaseViewModel() {

    private val loadLastSearchesUseCase by inject<LoadLastSearchesUseCase>()
    private val loadFilteredCities by inject<LoadFilteredCitiesUseCase>()
    private val saveLastSearchesUseCase by inject<SaveSearchedItemUseCase>()

    private var filteredCitiesLiveData: LiveData<List<CityDO>>? = null
    private var lastSearchesLiveData: LiveData<List<LastSearchItemDO>>? = null

    private val filteredCitiesObserver: Observer<List<CityDO>> by lazy {
        Observer<List<CityDO>> {
            viewStateEvent.value = CityPickerViewStates.AutoCompleteLoadedEvent(it.map {
                AutoCompleteMapper.mapFrom(from = it)
            })
        }
    }

    private val lastSearchesObserver: Observer<List<LastSearchItemDO>> by lazy {
        Observer<List<LastSearchItemDO>> {
            viewStateEvent.value = CityPickerViewStates.LastUsedItemsLoadedEvent(items = it.map {
                LastSearchesVOMapper.mapFrom(from = it)
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
            lastSearchesLiveData = loadLastSearchesUseCase(Unit)
            launch(Dispatchers.Main) { lastSearchesLiveData?.observeForever(lastSearchesObserver) }
        }
    }

    fun itemSelected(item: AutoCompleteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            saveLastSearchesUseCase(params = LastSearchItemDO(key = item.key, name = item.name))
        }
    }

    override fun onCleared() {
        super.onCleared()

        filteredCitiesLiveData?.removeObserver(filteredCitiesObserver)
        lastSearchesLiveData?.removeObserver(lastSearchesObserver)
    }

}