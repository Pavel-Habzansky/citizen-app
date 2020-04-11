package com.pavelhabzansky.data.features.issues.repository

import com.pavelhabzansky.data.core.USE_CONTEXT_CITIZEN
import com.pavelhabzansky.data.core.USE_CONTEXT_EMPTY
import com.pavelhabzansky.data.core.USE_CONTEXT_TOURIST
import com.pavelhabzansky.data.features.issues.dao.IssueDao
import com.pavelhabzansky.data.features.issues.mapper.IssueMapper
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.mapper.PlaceMapper
import com.pavelhabzansky.data.features.settings.dao.IssueSettingsDao
import com.pavelhabzansky.data.features.settings.dao.PlaceSettingsDao
import com.pavelhabzansky.domain.features.issues.domain.MapItemsDO
import com.pavelhabzansky.domain.features.issues.domain.assembleMapItems
import com.pavelhabzansky.domain.features.issues.repository.IMapItemsRepository

class MapItemsRepository(
        private val placeSettingsDao: PlaceSettingsDao,
        private val issueSettingsDao: IssueSettingsDao,
        private val placesDao: PlacesDao,
        private val issueDao: IssueDao
) : IMapItemsRepository {

    override suspend fun loadMapItems(useContext: String): MapItemsDO {
        val enabledPlaces = placeSettingsDao.getAllEnabled().map { it.type }
        val enabledIssues = issueSettingsDao.getAllEnabled().map { it.type }
        return when (useContext) {
            USE_CONTEXT_CITIZEN -> {
                val issues = issueDao.getAll()
                        .asSequence()
                        .map { IssueMapper.mapFrom(it) }
                        .filter { enabledIssues.contains(it.type.type) }
                        .toList()
                assembleMapItems(issues = issues)
            }
            USE_CONTEXT_TOURIST -> {
                val places = placesDao.getAll()
                        .asSequence()
                        .map { PlaceMapper.mapFrom(it) }
                        .filter { enabledPlaces.contains(it.type.type) }
                        .toList()
                assembleMapItems(places = places)
            }
            else -> {
                val issues = issueDao.getAll()
                        .asSequence()
                        .map { IssueMapper.mapFrom(it) }
                        .filter { enabledIssues.contains(it.type.type) }
                        .toList()
                val places = placesDao.getAll()
                        .asSequence()
                        .map { PlaceMapper.mapFrom(it) }
                        .filter { enabledPlaces.contains(it.type.type) }
                        .toList()
                assembleMapItems(issues, places)
            }
        }
    }

}