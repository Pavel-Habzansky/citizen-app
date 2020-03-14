package com.pavelhabzansky.domain.features.settings.repository

import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO

interface ISettingsRepository {

    suspend fun loadAllSettings(): SettingsDTO

    suspend fun loadSettings(): Set<PlaceTypeDO>

    suspend fun saveSettings(settings: SettingsDTO)

}