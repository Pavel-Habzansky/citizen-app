package com.pavelhabzansky.citizenapp.features.issues.detail.view.vm

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.issues.detail.states.IssueDetailErrorStates
import com.pavelhabzansky.citizenapp.features.issues.detail.states.IssueDetailViewStates
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.domain.features.issues.usecase.LoadIssueImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class IssueDetailViewModel : BaseViewModel() {

    private val loadIssueImageUseCase by inject<LoadIssueImageUseCase>()

    val issueDetailViewStates = SingleLiveEvent<IssueDetailViewStates>()
    val issueDetailErrorStates = SingleLiveEvent<IssueDetailErrorStates>()

    private val imageObserver: Observer<ByteArray> by lazy {
        Observer<ByteArray> {
            val image = BitmapFactory.decodeByteArray(it, 0, it.size)
            issueDetailViewStates.postValue(IssueDetailViewStates.IssueImageLoadedEvent(image))
        }
    }

    lateinit var issue: IssueVO

    private var byteData: LiveData<ByteArray>? = null

    fun loadIssueImage() {
        viewModelScope.launch(Dispatchers.IO) {
            byteData = loadIssueImageUseCase(LoadIssueImageUseCase.Params(issue.img))
            launch(Dispatchers.Main) { byteData?.observeForever(imageObserver) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        byteData?.removeObserver(imageObserver)
    }

}