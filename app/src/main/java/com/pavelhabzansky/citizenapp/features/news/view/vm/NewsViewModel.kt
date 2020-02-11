package com.pavelhabzansky.citizenapp.features.news.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.news.states.NewsErrorState
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.mapper.NewsVOMapper
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.usecase.LoadCachedNewsUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class NewsViewModel : BaseViewModel() {

    private val loadNewsUseCase by inject<LoadNewsUseCase>()
    private val loadCachedNewsUseCase by inject<LoadCachedNewsUseCase>()

    val newsViewState = SingleLiveEvent<NewsViewState>()
    val newsErrorState = SingleLiveEvent<NewsErrorState>()

    private val cachedNewsObserver: Observer<List<NewsDO>> by lazy {
        Observer<List<NewsDO>> {
            val viewObjects = it.map { NewsVOMapper.mapFrom(from = it) }
            newsViewState.postValue(NewsViewState.NewsCacheLoadedViewState(news = viewObjects))
        }
    }

    private var cachedNewsLiveData: LiveData<List<NewsDO>>? = null

    fun loadCachedNews() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedNewsLiveData = loadCachedNewsUseCase(Unit)
            launch(Dispatchers.Main) { cachedNewsLiveData?.observeForever(cachedNewsObserver) }
        }
    }

    fun loadNews(force: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            loadNewsUseCase(LoadNewsUseCase.Params(force))
        }
    }

    override fun onCleared() {
        super.onCleared()

        cachedNewsLiveData?.removeObserver(cachedNewsObserver)
    }

}