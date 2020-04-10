package com.pavelhabzansky.domain.features.cities.usecase

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.core.UseCase
import com.pavelhabzansky.domain.features.cities.domain.CityGalleryItemDO
import com.pavelhabzansky.domain.features.cities.repository.ICityRepository

class GetPhotogalleryUseCase(
        private val cityRepository: ICityRepository
) : UseCase<LiveData<List<CityGalleryItemDO>>, String>() {

    override suspend fun doWork(params: String): LiveData<List<CityGalleryItemDO>> {
        return cityRepository.getCityPhotogallery(params)
    }

}