package com.pavelhabzansky.citizenapp.features.news.view.vm

import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.domain.features.news.usecase.ConnectFirebaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class NewsViewModel : BaseViewModel() {

    private val connectFirebaseUseCase by inject<ConnectFirebaseUseCase>()

    fun callFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            connectFirebaseUseCase(Unit)
        }
    }

}