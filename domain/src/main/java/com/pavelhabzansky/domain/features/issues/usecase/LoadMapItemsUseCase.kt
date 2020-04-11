package com.pavelhabzansky.domain.features.issues.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.issues.domain.MapItemsDO
import com.pavelhabzansky.domain.features.issues.repository.IMapItemsRepository

class LoadMapItemsUseCase(
        private val mapItemsRepository: IMapItemsRepository
) : UseCase<MapItemsDO, LoadMapItemsUseCase.Params>() {

    override suspend fun doWork(params: Params): MapItemsDO {
        return mapItemsRepository.loadMapItems(params.useContext)
    }

    data class Params(
            val useContext: String
    )

}