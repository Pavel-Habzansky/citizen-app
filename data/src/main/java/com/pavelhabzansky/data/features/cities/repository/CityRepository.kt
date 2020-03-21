package com.pavelhabzansky.data.features.cities.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.pavelhabzansky.data.core.*
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.cities.dao.LastSearchDao
import com.pavelhabzansky.data.features.cities.entities.CityEntity
import com.pavelhabzansky.data.features.cities.mapper.CityMapper
import com.pavelhabzansky.data.features.cities.mapper.LastSearchMapper
import com.pavelhabzansky.data.features.news.dao.NewsDao
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository
import com.pavelhabzansky.domain.features.cities.usecase.SetCityResidentialUseCase
import timber.log.Timber

class CityRepository(
    private val cityReference: DatabaseReference,
    private val storageReference: StorageReference,
    private val lastSearchDao: LastSearchDao,
    private val newsDao: NewsDao,
    private val cityDao: CityDao
) : ICityRepository {

    override suspend fun loadLastSearches(): LiveData<List<LastSearchItemDO>> {
        val lastSearches = lastSearchDao.getLastSearchesLD()
        return lastSearches.transform {
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
                        id = requireNotNull(it.child(CITY_CHILD_ID).value.toString()),
                        name = requireNotNull(it.child(CITY_CHILD_NAME).value.toString())
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
                val name = snapshot.child(CITY_CHILD_NAME).value?.toString() ?: ""
                val id = snapshot.child(CITY_CHILD_ID).value?.toString() ?: ""
                val www = snapshot.child(CITY_CHILD_WWW).value?.toString() ?: ""
                val rssFeed = snapshot.child(CITY_CHILD_RSS_FEED).value?.toString() ?: ""
                val rssUrl = snapshot.child(CITY_CHILD_RSS_URL).value?.toString() ?: ""
                val wikiInfo = snapshot.child(CITY_CHILD_WIKI)
                val population = wikiInfo.child(WIKI_CHILD_CITIZENS).value?.toString()?.toLong()
                val description = wikiInfo.child(WIKI_CHILD_HEADLINE).value?.toString()
                val logo = wikiInfo.child(WIKI_CHILD_LOGO).value?.toString()
                val lat = wikiInfo.child(WIKI_CHILD_GPS).child(GPS_CHILD_LAT).value?.toString()
                    ?.toDoubleOrNull()
                val lng = wikiInfo.child(WIKI_CHILD_GPS).child(GPS_CHILD_LNG).value?.toString()
                    ?.toDoubleOrNull()

                val cityObject = CityInformationDO(
                    key = cityKey,
                    id = id,
                    name = name,
                    population = population,
                    lat = lat,
                    lng = lng,
                    description = description,
                    www = www,
                    rssFeed = rssFeed,
                    rssUrl = rssUrl
                )

                cityInfo.postValue(cityObject)

                logo?.let { logo ->
                    val logoReference = storageReference.child(logo)
                    logoReference.getBytes(IMG_MAX_SIZE).addOnSuccessListener {
                        Timber.i("Loaded ${it.size} bytes for city logo")
                        cityInfo.postValue(
                            cityObject.copy(logoBytes = it)
                        )
                    }.addOnFailureListener {
                        Timber.i("Unable to load logo: $it")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })

        return cityInfo
    }

    override suspend fun setAsResidential(city: CityInformationDO) {
        val cityEntity = CityEntity(
            key = city.key,
            id = city.id,
            name = city.name,
            residential = true,
            rssFeed = city.rssFeed ?: "",
            rssUrl = city.rssUrl ?: ""
        )

        cityDao.unsetResidential()
        cityDao.insert(entity = cityEntity)
        newsDao.removeAll()
    }

    override suspend fun getResidentialCity(): CityDO? {
        val residential = cityDao.getResidential()
        return residential?.let { CityMapper.mapFrom(from = residential) }
    }

}