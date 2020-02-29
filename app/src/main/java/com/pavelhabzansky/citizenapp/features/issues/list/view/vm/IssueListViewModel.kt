package com.pavelhabzansky.citizenapp.features.issues.list.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListErrorStates
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListViewStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueListViewModel : BaseViewModel() {

    val issueListViewState = SingleLiveEvent<IssueListViewStates>()
    val issueListErrorState = SingleLiveEvent<IssueListErrorStates>()

    fun loadIssues() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

}