package com.pavelhabzansky.domain.features.settings.usecase

import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO
import com.pavelhabzansky.domain.features.settings.repository.ISettingsRepository

class LoadAllSettingsUseCase(
        private val settingsRepository: ISettingsRepository
) : UseCase<SettingsDTO, Unit>() {

    override suspend fun doWork(params: Unit): SettingsDTO {
        return settingsRepository.loadAllSettings()
    }

}