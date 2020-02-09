package com.pavelhabzansky.domain.features.news.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.news.domain.NewsDO

interface INewsRepository {

    suspend fun loadNews()

    suspend fun loadCachedNews(): LiveData<List<NewsDO>>

}