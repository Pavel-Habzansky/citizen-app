package com.pavelhabzansky.citizenapp.features.events.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.events.states.PushErrorStates
import com.pavelhabzansky.citizenapp.features.events.states.PushViewStates
import com.pavelhabzansky.citizenapp.features.events.view.vo.PushEventVO
import com.pavelhabzansky.citizenapp.features.events.view.vo.mapper.PushEventVOMapper
import com.pavelhabzansky.domain.features.events.domain.PushEvent
import com.pavelhabzansky.domain.features.events.usecase.DeletePushEventUseCase
import com.pavelhabzansky.domain.features.events.usecase.LoadInboxUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class PushEventListViewModel : BaseViewModel() {

    private val loadInbox by inject<LoadInboxUseCase>()
    private val removeInbox by inject<DeletePushEventUseCase>()

    private val inboxObserver: Observer<List<PushEvent>> by lazy {
        Observer<List<PushEvent>> {
            val views = it.map { PushEventVOMapper.mapFrom(it) }
            pushViewStates.postValue(PushViewStates.PushEventsLoaded(views))
        }
    }

    private var inbox: LiveData<List<PushEvent>>? = null

    val pushViewStates = SingleLiveEvent<PushViewStates>()
    val pushErrorStates = SingleLiveEvent<PushErrorStates>()

    fun loadInbox() {
        viewModelScope.launch(Dispatchers.IO) {
            inbox = loadInbox(Unit)
            launch(Dispatchers.Main) { inbox?.observeForever(inboxObserver) }
        }
    }

    fun removeEvent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeInbox(id)
        }
    }

    override fun onCleared() {
        super.onCleared()

        inbox?.removeObserver(inboxObserver)
    }

}