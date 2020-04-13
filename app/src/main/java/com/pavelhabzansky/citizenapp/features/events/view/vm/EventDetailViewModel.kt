package com.pavelhabzansky.citizenapp.features.events.view.vm

import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.events.states.EventsErrorStates
import com.pavelhabzansky.citizenapp.features.events.states.EventsViewStates
import com.pavelhabzansky.domain.features.events.usecase.DownloadGalleryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import kotlin.Exception

class EventDetailViewModel : BaseViewModel() {

    private val downloadGallery by inject<DownloadGalleryUseCase>()

    val eventDetailViewState = SingleLiveEvent<EventsViewStates>()
    val errorViewState = SingleLiveEvent<EventsErrorStates>()

    fun loadGallery(sources: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gallery = downloadGallery(sources)
                val images = gallery.map { BitmapFactory.decodeByteArray(it, 0, it.size) }
                eventDetailViewState.postValue(EventsViewStates.GalleryDownloaded(images))
            } catch (e: Exception) {
                errorViewState.postValue(EventsErrorStates.UnexpectedErrorEvent(e))
            }
        }
    }


}