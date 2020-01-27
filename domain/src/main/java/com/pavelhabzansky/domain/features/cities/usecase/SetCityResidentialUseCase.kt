package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class SetCityResidentialUseCase(
    private val cityRepository: ICityRepository
) : UseCase<SetCityResidentialUseCase.UseCaseResult, SetCityResidentialUseCase.Params>() {

    override suspend fun doWork(params: Params): UseCaseResult {
        val residential = cityRepository.getResidentialCity()
        return if (residential == null) {
            cityRepository.setAsResidential(key = params.key, id = params.id, name = params.name)
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.RESIDENTIAL_EXISTS
        }
    }

    data class Params(
        val key: String,
        val name: String,
        val id: String
    )

    enum class UseCaseResult {
        SUCCESS, RESIDENTIAL_EXISTS
    }
}