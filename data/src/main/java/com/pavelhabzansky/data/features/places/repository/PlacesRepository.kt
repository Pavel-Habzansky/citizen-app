package com.pavelhabzansky.data.features.places.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.pavelhabzansky.data.core.transform
import com.pavelhabzansky.data.features.api.PlacesApi
import com.pavelhabzansky.data.features.settings.dao.PlaceSettingsDao
import com.pavelhabzansky.data.features.places.dao.PlacesDao
import com.pavelhabzansky.data.features.places.entities.PhotoEntity
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.data.features.places.mapper.PhotoMapper
import com.pavelhabzansky.data.features.settings.entities.PlaceSettingsEntity
import com.pavelhabzansky.data.features.places.mapper.PlaceMapper
import com.pavelhabzansky.domain.features.places.domain.PlaceDO
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO
import com.pavelhabzansky.domain.features.places.repository.IPlacesRepository
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase
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
        val photoEntities = mutableListOf<PhotoEntity>()
        enabledTypes.forEach { type ->
            val call = placesApi.fetchPlaces(
                    location = "${location.lat},${location.lng}",
                    key = "AIzaSyBnhRiB4J1pHrFa7Ex8p4jIcoLElac6JAM",
                    type = type.type,
                    pageToken = ""
            )

            val response = call.execute()

            val body = response.body()

            body?.results?.let {
                it.forEach {
                    val place = PlaceMapper.mapApiToEntity(api = it).also { it.type = type.type }
                    entities.add(place)
                    val photos = it.photos.map { PhotoMapper.mapFrom(from = it).also { it.placeId = place.placeId } }
                    photoEntities.addAll(photos)
                }
            }
        }

        placesDao.removeAll()
        placesDao.insertAll(entities.distinctBy { it.placeId })
        placesDao.insertPhotos(photoEntities)
    }

    override suspend fun loadAllPlaces(): LiveData<List<PlaceDO>> {
        if (placeSettingsDao.getCount() != PlaceTypeDO.values().size) {
            populatePlaceSettings()
        }

        val enabledTypes = placeSettingsDao.getAllEnabled().map { it.type }
        val allPlaces = placesDao.getAllLive()

        return allPlaces.transform {
            it.asSequence()
                    .filter { enabledTypes.contains(it.type) }
                    .map { PlaceMapper.mapFrom(from = it) }
                    .toList()
        }
    }

    private fun populatePlaceSettings() {
        val settings = PlaceTypeDO.values().map {
            PlaceSettingsEntity(type = it.type, enabled = true)
        }

        placeSettingsDao.insertAll(entities = settings)
    }

    override suspend fun loadImage(placeId: String): List<ByteArray> {
        val placePhoto = placesDao.getPhotos(placeId)
        val gallery = mutableListOf<ByteArray>()
        placePhoto.forEach {
            val call = placesApi.fetchImage(
                    photoRef = it.photoRef,
                    key = "AIzaSyBnhRiB4J1pHrFa7Ex8p4jIcoLElac6JAM"
            )

            val response = call.execute()

            response.body()?.byteStream()?.readBytes()?.let {
                gallery.add(it)
            }
        }

        return gallery
    }

}