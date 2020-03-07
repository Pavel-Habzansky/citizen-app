package com.pavelhabzansky.data.features.places.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.data.core.transform
import com.pavelhabzansky.data.features.api.PlacesApi
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.mapper.PlaceMapper
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase
import timber.log.Timber

class PlacesRepository(
        private val placesApi: PlacesApi,
        private val placesDao: PlacesDao
) : IPlacesRepository {

    override suspend fun fetchPlaces(location: FetchPlacesUseCase.Params) {

        val call = placesApi.fetchPlaces(
                location = "${location.lat},${location.lng}",
                type = "bar",
                key = "AIzaSyBnhRiB4J1pHrFa7Ex8p4jIcoLElac6JAM"
        )

        val response = call.execute()
        Timber.i(response.toString())

        response.body()?.let {
            val entities = it.results.map { PlaceMapper.mapApiToEntity(api = it) }

            placesDao.removeAll()
            placesDao.insertAll(entities)
        }
    }

    override suspend fun loadAllPlaces(): LiveData<List<PlaceDO>> {
        val allPlaces = placesDao.getAll()
        return allPlaces.transform { it.map { PlaceMapper.mapFrom(from = it) } }
    }


}