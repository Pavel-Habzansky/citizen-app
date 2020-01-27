package com.pavelhabzansky.domain.features.cities.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class LoadCityInfoUseCase(
    private val cityRepository: ICityRepository
) : UseCase<LiveData<CityInformationDO>, String>() {

    override suspend fun doWork(params: String): LiveData<CityInformationDO> {
        return cityRepository.loadCityInformation(cityKey = params)
    }

}