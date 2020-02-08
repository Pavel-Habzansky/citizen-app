package com.pavelhabzansky.citizenapp.features.news.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.domain.features.news.usecase.ConnectFirebaseUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class NewsViewModel : BaseViewModel() {

    private val connectFirebaseUseCase by inject<ConnectFirebaseUseCase>()
    private val loadNewsUseCase by inject<LoadNewsUseCase>()

    fun callFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            connectFirebaseUseCase(Unit)
        }
    }

    fun loadNews() {
        viewModelScope.launch(Dispatchers.IO) {
            loadNewsUseCase(Unit)
        }
    }

}