package com.pavelhabzansky.citizenapp.features.issues.list.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.USE_CONTEXT_EMPTY
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.di.ISSUES_LIST_MODULE
import com.pavelhabzansky.citizenapp.features.issues.list.states.MapListErrorStates
import com.pavelhabzansky.citizenapp.features.issues.list.states.MapListViewStates
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.mapItemsFromDomain
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadMapItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import org.koin.core.qualifier.named

class MapListViewModel : BaseViewModel() {

    private val loadIssues by inject<LoadMapItemsUseCase>()
    private val fetchIssues by inject<FetchIssuesUseCase>(qualifier = named(ISSUES_LIST_MODULE))

    val mapListViewState = SingleLiveEvent<MapListViewStates>()
    val mapListErrorState = SingleLiveEvent<MapListErrorStates>()

    private var issuesLiveData: LiveData<List<IssueDO>>? = null

    private val issuesObserver: Observer<List<IssueDO>> by lazy {
        Observer<List<IssueDO>> {
            val issueViews = it.map { IssueVOMapper.mapTo(to = it) }
            mapListViewState.postValue(MapListViewStates.IssueLoadedEvent(issueViews))
        }
    }

    var useContext: String = USE_CONTEXT_EMPTY

    fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
//            issuesLiveData = loadIssues(LoadMapItemsUseCase.Params(useContext))
            launch(Dispatchers.Main) { issuesLiveData?.observeForever(issuesObserver) }

            val mapItems = loadIssues(LoadMapItemsUseCase.Params(useContext))
            val mapitemsView = mapItemsFromDomain(mapItems)

        }
    }

    fun syncIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchIssues(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()

        issuesLiveData?.removeObserver(issuesObserver)
    }

}