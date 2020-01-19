package com.pavelhabzansky.domain.features.cities.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class SaveSearchedItemUseCase(
    private val cityRepository: ICityRepository
) : UseCase<Unit, LastSearchItemDO>() {

    override suspend fun doWork(params: LastSearchItemDO) {
        cityRepository.saveSearch(search = params)
    }

}