package com.pavelhabzansky.domain.features.cities.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class LoadFilteredCitiesUseCase(
    private val cityRepository: ICityRepository
) : UseCase<LiveData<List<CityDO>>, String>() {

    override suspend fun doWork(params: String): LiveData<List<CityDO>> {
        return cityRepository.loadCitiesBy(startsWith = params)
    }

}