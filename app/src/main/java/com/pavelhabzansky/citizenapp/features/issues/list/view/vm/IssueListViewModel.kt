package com.pavelhabzansky.citizenapp.features.issues.list.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.di.ISSUES_LIST_MODULE
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListErrorStates
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadIssuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import org.koin.core.qualifier.named

class IssueListViewModel : BaseViewModel() {

    private val loadIssuesUseCase by inject<LoadIssuesUseCase>()
    private val fetchIssuesUseCase by inject<FetchIssuesUseCase>(qualifier = named(ISSUES_LIST_MODULE))

    val issueListViewState = SingleLiveEvent<IssueListViewStates>()
    val issueListErrorState = SingleLiveEvent<IssueListErrorStates>()

    private var issuesLiveData: LiveData<List<IssueDO>>? = null

    private val issuesObserver: Observer<List<IssueDO>> by lazy {
        Observer<List<IssueDO>> {
            val issueViews = it.map { IssueVOMapper.mapTo(to = it) }
            issueListViewState.postValue(IssueListViewStates.IssueLoadedEvent(issueViews))
        }
    }

    fun loadIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            issuesLiveData = loadIssuesUseCase(Unit)
            launch(Dispatchers.Main) { issuesLiveData?.observeForever(issuesObserver) }
        }
    }

    fun syncIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchIssuesUseCase(Unit)
        }
    }

}