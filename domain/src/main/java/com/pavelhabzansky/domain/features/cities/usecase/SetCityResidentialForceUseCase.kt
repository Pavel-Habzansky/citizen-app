package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class SetCityResidentialForceUseCase(
    private val cityRepository: ICityRepository
) : UseCase<Unit, SetCityResidentialForceUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        cityRepository.setAsResidential(city = params.city)
    }

    data class Params(
        val city: CityInformationDO
    )
}