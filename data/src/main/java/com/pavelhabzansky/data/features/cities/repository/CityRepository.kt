package com.pavelhabzansky.data.features.cities.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pavelhabzansky.data.features.cities.dao.LastSearchDao
import com.pavelhabzansky.data.features.cities.mapper.LastSearchMapper
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import timber.log.Timber

class CityRepository(
    private val cityReference: DatabaseReference,
    private val lastSearchDao: LastSearchDao
) : ICityRepository {

    override suspend fun loadLastSearches(): List<LastSearchItemDO> {
        val lastSearches = lastSearchDao.getLastSearches()
        return lastSearches.map { LastSearchMapper.mapFrom(from = it) }
    }

    override suspend fun loadCitiesBy(startsWith: String): LiveData<List<CityDO>> {
        val cities = MutableLiveData<List<CityDO>>()
        cityReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cityList = snapshot.children.map {
                    Timber.i("Loaded: ${it.key}")

                    CityDO(
                        key = requireNotNull(it.key),
                        id = requireNotNull(it.child("id").value.toString()),
                        name = requireNotNull(it.child("name").value.toString())
                    )

                }

                cities.postValue(cityList)
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })

        return cities
    }

}