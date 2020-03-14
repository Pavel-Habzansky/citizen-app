package com.pavelhabzansky.domain.features.places.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository

class LoadPlacesUseCase(
        private val placesRepository: IPlacesRepository
) : UseCase<LiveData<List<PlaceDO>>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<List<PlaceDO>> {
        return placesRepository.loadAllPlaces()
    }

}