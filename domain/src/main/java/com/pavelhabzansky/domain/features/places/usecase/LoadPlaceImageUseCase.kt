package com.pavelhabzansky.domain.features.places.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository

class LoadPlaceImageUseCase(
        private val placesRepository: IPlacesRepository
) : UseCase<List<ByteArray>, String>() {

    override suspend fun doWork(params: String): List<ByteArray> {
        return placesRepository.loadImage(params)
    }

}