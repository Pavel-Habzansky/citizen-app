package com.pavelhabzansky.domain.features.cities.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.CityGalleryItemDO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.cities.usecase.SetCityResidentialUseCase

interface ICityRepository {

    suspend fun loadLastSearches(): LiveData<List<LastSearchItemDO>>

    suspend fun loadCitiesBy(startsWith: String): LiveData<List<CityDO>>

    suspend fun saveSearch(search: LastSearchItemDO)

    suspend fun loadCityInformation(cityKey: String): LiveData<CityInformationDO>

    suspend fun setAsResidential(city: CityInformationDO)

    suspend fun getResidentialCity(): CityDO?

    suspend fun getCityPhotogallery(key: String): LiveData<List<CityGalleryItemDO>>

}