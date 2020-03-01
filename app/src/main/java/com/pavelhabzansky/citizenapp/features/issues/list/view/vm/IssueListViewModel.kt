package com.pavelhabzansky.citizenapp.features.issues.list.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListErrorStates
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.domain.features.issues.usecase.LoadIssuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class IssueListViewModel : BaseViewModel() {

    private val loadIssuesUseCase by inject<LoadIssuesUseCase>()

    val issueListViewState = SingleLiveEvent<IssueListViewStates>()
    val issueListErrorState = SingleLiveEvent<IssueListErrorStates>()

    fun loadIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            val issues = loadIssuesUseCase(Unit)
            val issueViews = issues.map { IssueVOMapper.mapTo(to = it) }
            issueListViewState.postValue(IssueListViewStates.IssueLoadedEvent(issueViews))
        }
    }

}