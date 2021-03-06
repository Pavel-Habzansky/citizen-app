package com.pavelhabzansky.domain.features.cities.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class LoadLastSearchesUseCase(
    private val cityRepository: ICityRepository
) : UseCase<LiveData<List<LastSearchItemDO>>, Unit>() {

    override suspend fun doWork(params: Unit): LiveData<List<LastSearchItemDO>> {
        return cityRepository.loadLastSearches()
    }

}