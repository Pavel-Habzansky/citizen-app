package com.pavelhabzansky.data.features.places.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.data.core.transform
import com.pavelhabzansky.data.features.api.PlacesApi
import com.pavelhabzansky.data.features.api.PlacesSearchResult
import com.pavelhabzansky.data.features.places.dao.PlaceSettingsDao
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.data.features.places.entities.PlaceSettingsEntity
import com.pavelhabzansky.data.features.places.mapper.PlaceMapper
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase
import kotlinx.coroutines.delay
import timber.log.Timber

class PlacesRepository(
        private val placesApi: PlacesApi,
        private val placesDao: PlacesDao,
        private val placeSettingsDao: PlaceSettingsDao
) : IPlacesRepository {

    override suspend fun fetchPlaces(location: FetchPlacesUseCase.Params) {
        if (placeSettingsDao.getCount() != PlaceTypeDO.values().size) {
            populatePlaceSettings()
        }

        val enabledTypes = placeSettingsDao.getAllEnabled()
        val entities = mutableListOf<PlaceEntity>()
        enabledTypes.forEach { type ->
            val call = placesApi.fetchPlaces(
                    location = "${location.lat},${location.lng}",
                    key = "AIzaSyBnhRiB4J1pHrFa7Ex8p4jIcoLElac6JAM",
                    type = type.type,
                    pageToken = ""
            )

            val response = call.execute()
            Timber.i(response.toString())

            val body = response.body()
            Timber.i("${body?.results?.size} results for type $type")

            body?.results?.let {
                entities.addAll(it.map { PlaceMapper.mapApiToEntity(api = it).also { it.type = type.type } })
            }
        }

        placesDao.removeAll()
        placesDao.insertAll(entities.distinctBy { it.placeId })
    }

    override suspend fun loadAllPlaces(): LiveData<List<PlaceDO>> {
        val allPlaces = placesDao.getAll()
        return allPlaces.transform { it.map { PlaceMapper.mapFrom(from = it) } }
    }

    private fun populatePlaceSettings() {
        val settings = PlaceTypeDO.values().map {
            PlaceSettingsEntity(type = it.type, enabled = true)
        }

        placeSettingsDao.insertAll(entities = settings)
    }

}