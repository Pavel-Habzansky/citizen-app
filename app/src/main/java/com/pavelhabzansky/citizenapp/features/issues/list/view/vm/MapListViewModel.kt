package com.pavelhabzansky.citizenapp.features.issues.list.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.USE_CONTEXT_EMPTY
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.states.MapListErrorStates
import com.pavelhabzansky.citizenapp.features.issues.list.states.MapListViewStates
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.mapItemsFromDomain
import com.pavelhabzansky.domain.features.issues.usecase.LoadMapItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class MapListViewModel : BaseViewModel() {

    private val loadMapItems by inject<LoadMapItemsUseCase>()

    val mapListViewState = SingleLiveEvent<MapListViewStates>()
    val mapListErrorState = SingleLiveEvent<MapListErrorStates>()

    var useContext: String = USE_CONTEXT_EMPTY

    fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val mapItems = loadMapItems(LoadMapItemsUseCase.Params(useContext))
            val mapitemsView = mapItemsFromDomain(mapItems)

            mapListViewState.postValue(MapListViewStates.MapItemsLoaded(mapitemsView))
        }
    }

}