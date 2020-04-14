package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class SaveFilterUseCase(
        private val eventsRepository: IEventsRepository
) : UseCase<Unit, List<CitySettingDO>>() {

    override suspend fun doWork(params: List<CitySettingDO>) {
        eventsRepository.saveFilter(params)
    }

}