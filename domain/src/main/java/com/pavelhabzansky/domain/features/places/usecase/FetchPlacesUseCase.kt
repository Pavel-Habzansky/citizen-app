package com.pavelhabzansky.domain.features.places.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository

class FetchPlacesUseCase(
        private val placesRepository: IPlacesRepository
) : UseCase<Unit, FetchPlacesUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        placesRepository.fetchPlaces(params)
    }

    data class Params(
            val lat: Double,
            val lng: Double
    )
}