package com.pavelhabzansky.domain.features.settings.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO
import com.pavelhabzansky.domain.features.settings.repository.ISettingsRepository

class SaveSettingsUseCase(
        private val settingsRepository: ISettingsRepository
) : UseCase<Unit, SaveSettingsUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        settingsRepository.saveSettings(settings = params.settings)
    }

    data class Params(
            val settings: SettingsDTO
    )

}