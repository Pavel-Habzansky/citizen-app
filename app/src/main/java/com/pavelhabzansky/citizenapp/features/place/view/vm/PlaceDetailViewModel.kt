package com.pavelhabzansky.citizenapp.features.place.view.vm

import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.place.states.PlaceDetailErrorStates
import com.pavelhabzansky.citizenapp.features.place.states.PlaceDetailViewStates
import com.pavelhabzansky.domain.features.places.usecase.LoadPlaceImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class PlaceDetailViewModel : BaseViewModel() {

    private val loadPlaceImageUseCase by inject<LoadPlaceImageUseCase>()

    val placeDetailViewState = SingleLiveEvent<PlaceDetailViewStates>()
    val placeDetailErrorState = SingleLiveEvent<PlaceDetailErrorStates>()

    fun loadPlaceImage(placeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val gallery = loadPlaceImageUseCase(placeId)
            val bitmaps = gallery.map { BitmapFactory.decodeByteArray(it, 0, it.size) }
            placeDetailViewState.postValue(PlaceDetailViewStates.GalleryLoadedState(bitmaps))
        }
    }

}