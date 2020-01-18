package com.pavelhabzansky.domain.features.cities.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO

interface ICityRepository {

    suspend fun loadLastSearches(): List<LastSearchItemDO>

    suspend fun loadCitiesBy(startsWith: String): LiveData<List<CityDO>>

}