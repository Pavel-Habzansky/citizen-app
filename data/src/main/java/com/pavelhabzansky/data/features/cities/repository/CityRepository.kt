package com.pavelhabzansky.data.features.cities.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pavelhabzansky.data.features.cities.dao.LastSearchDao
import com.pavelhabzansky.data.features.cities.mapper.LastSearchMapper
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import timber.log.Timber

class CityRepository(
    private val cityReference: DatabaseReference,
    private val lastSearchDao: LastSearchDao
) : ICityRepository {

    override suspend fun loadLastSearches(): LiveData<List<LastSearchItemDO>> {
        val lastSearches = lastSearchDao.getLastSearches()
        return Transformations.map(lastSearches) {
            it.map { LastSearchMapper.mapFrom(from = it) }
        }
    }

    override suspend fun loadCitiesBy(startsWith: String): LiveData<List<CityDO>> {
        val cities = MutableLiveData<List<CityDO>>()
        cityReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cityList = snapshot.children.map {

                    CityDO(
                        key = requireNotNull(it.key),
                        id = requireNotNull(it.child("id").value.toString()),
                        name = requireNotNull(it.child("name").value.toString())
                    )

                }

                cities.postValue(cityList.filter {
                    it.name.startsWith(
                        prefix = startsWith,
                        ignoreCase = true
                    )
                })
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })

        return cities
    }

    override suspend fun saveSearch(search: LastSearchItemDO) {
        val entity = LastSearchMapper.mapTo(to = search)
        lastSearchDao.insert(entity = entity)
    }

    override suspend fun loadCityInformation(cityKey: String): LiveData<CityInformationDO> {
        val cityInfo = MutableLiveData<CityInformationDO>()
        val city = cityReference.child(cityKey)
        city.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value?.toString()
                val wikiInfo = snapshot.child("wiki")
                val population = wikiInfo.child("citizens").value?.toString()?.toLong()
                val description = wikiInfo.child("headline").value?.toString()

                cityInfo.postValue(
                    CityInformationDO(
                        key = cityKey,
                        name = name,
                        population = population,
                        description = description
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })

        return cityInfo
    }

}