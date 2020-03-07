package com.pavelhabzansky.domain.features.places.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase

interface IPlacesRepository {

    suspend fun fetchPlaces(location: FetchPlacesUseCase.Params)

    suspend fun loadAllPlaces(): LiveData<List<PlaceDO>>

}