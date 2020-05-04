package com.pavelhabzansky.citizenapp.features.issues.myissues.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.myissues.state.MyIssuesErrorEvents
import com.pavelhabzansky.citizenapp.features.issues.myissues.state.MyIssuesViewStates
import com.pavelhabzansky.citizenapp.features.issues.myissues.view.mapper.UserIssueMapper
import com.pavelhabzansky.domain.features.issues.domain.MyIssueDO
import com.pavelhabzansky.domain.features.issues.usecase.LoadUserIssuesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class MyIssuesViewModel : BaseViewModel() {

    private val loadUserIssues by inject<LoadUserIssuesUseCase>()

    private val issuesObserver: Observer<List<MyIssueDO>> by lazy {
        Observer<List<MyIssueDO>> {
            val viewIssues = it.map { UserIssueMapper.mapFrom(it) }
            myIssuesViewState.postValue(MyIssuesViewStates.UserIssuesLoadedEvent(viewIssues))
        }
    }

    private var issues: LiveData<List<MyIssueDO>>? = null

    val myIssuesViewState = SingleLiveEvent<MyIssuesViewStates>()
    val myIssuesErrorState = SingleLiveEvent<MyIssuesErrorEvents>()

    fun loadMyIssues() {
        viewModelScope.launch(Dispatchers.IO) {
            issues = loadUserIssues(Unit)
            launch(Dispatchers.Main) { issues?.observeForever(issuesObserver) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        issues?.removeObserver(issuesObserver)
    }

}