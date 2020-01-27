package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class GetResidentialCityNameUseCase(
    private val cityRepository: ICityRepository
) : UseCase<String, Unit>(){

    override suspend fun doWork(params: Unit): String {
        return requireNotNull(cityRepository.getResidentialCity()).name
    }

}