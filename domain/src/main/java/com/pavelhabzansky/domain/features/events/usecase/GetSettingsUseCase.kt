package com.pavelhabzansky.domain.features.events.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.events.domain.CitySettingDO
import com.pavelhabzansky.domain.features.events.repository.IEventsRepository

class GetSettingsUseCase(
        private val eventRepository: IEventsRepository
) : UseCase<List<CitySettingDO>, Unit>() {

    override suspend fun doWork(params: Unit): List<CitySettingDO> {
        return eventRepository.getFilterSettings()
    }

}