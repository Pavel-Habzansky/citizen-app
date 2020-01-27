package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class GetResidentialCityUseCase(
    private val cityRepository: ICityRepository
) : UseCase<CityDO?, Unit>() {

    override suspend fun doWork(params: Unit): CityDO? {
        return cityRepository.getResidentialCity()
    }

}