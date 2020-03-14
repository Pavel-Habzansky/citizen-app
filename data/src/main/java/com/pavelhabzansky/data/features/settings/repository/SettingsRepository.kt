package com.pavelhabzansky.data.features.settings.repository

import com.pavelhabzansky.data.features.settings.dao.IssueSettingsDao
import com.pavelhabzansky.data.features.settings.dao.PlaceSettingsDao
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.settings.domain.SettingsDTO
import com.pavelhabzansky.domain.features.settings.repository.ISettingsRepository

class SettingsRepository(
        private val placeSettingsDao: PlaceSettingsDao,
        private val issueSettingsDao: IssueSettingsDao
) : ISettingsRepository {

    override suspend fun loadAllSettings(): SettingsDTO {
        val issueSettings = issueSettingsDao.getAllEnabled().map { IssueType.fromString(type = it.type) }.toSet()
        val placeSettings = placeSettingsDao.getAllEnabled().map { PlaceTypeDO.fromString(type = it.type) }.toSet()

        return SettingsDTO(placeSettings, issueSettings)
    }

    override suspend fun loadSettings(): Set<PlaceTypeDO> {
        val placeSettings = placeSettingsDao.getAllEnabled()
        return placeSettings.map { PlaceTypeDO.fromString(type = it.type) }.toSet()
    }

    override suspend fun saveSettings(settings: SettingsDTO) {
        val places = settings.placeSettings
        placeSettingsDao.setSetting(places.map { it.type }, true)
        placeSettingsDao.setSetting(PlaceTypeDO.values().asList().minus(places).map { it.type }, false)

        val issues = settings.issueSettings
        issueSettingsDao.setSetting(issues.map { it.type }, true)
        issueSettingsDao.setSetting(IssueType.values().asList().minus(issues).map { it.type }, false)
    }

}