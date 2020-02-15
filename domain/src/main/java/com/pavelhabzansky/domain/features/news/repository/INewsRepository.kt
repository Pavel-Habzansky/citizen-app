package com.pavelhabzansky.domain.features.news.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.news.domain.NewsDO

interface INewsRepository {

    suspend fun getNews(city: CityInformationDO): List<NewsDO>

    suspend fun loadNews(force: Boolean)

    suspend fun loadCachedNews(): LiveData<List<NewsDO>>

    suspend fun loadNewsItem(title: String): LiveData<NewsDO>

}