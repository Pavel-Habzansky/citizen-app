package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class SetCityResidentialForceUseCase(
    private val cityRepository: ICityRepository
) : UseCase<Unit, SetCityResidentialForceUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        cityRepository.setAsResidential(key = params.key, id = params.id, name = params.name)
    }

    data class Params(
        val key: String,
        val name: String,
        val id: String
    )
}