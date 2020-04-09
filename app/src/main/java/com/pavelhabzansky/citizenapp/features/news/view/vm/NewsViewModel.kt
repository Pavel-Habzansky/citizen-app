package com.pavelhabzansky.citizenapp.features.news.view.vm

import android.Manifest
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper.CityInformationVOMapper
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.citizenapp.features.map.states.MapViewStates
import com.pavelhabzansky.citizenapp.features.news.states.NewsErrorState
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.mapper.NewsVOMapper
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.usecase.LoadCachedNewsUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsForCityUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadTouristNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class NewsViewModel(app: Application) : BaseAndroidViewModel(app) {

    private val loadNewsUseCase by inject<LoadNewsUseCase>()
    private val loadTouristNewsUseCase by inject<LoadTouristNewsUseCase>()
    private val loadCachedNewsUseCase by inject<LoadCachedNewsUseCase>()
    private val loadNewsForCityUseCase by inject<LoadNewsForCityUseCase>()

    val newsViewState = SingleLiveEvent<NewsViewState>()
    val touristNewsViewState = SingleLiveEvent<NewsViewState>()
    val citizenNewsViewState = SingleLiveEvent<NewsViewState>()
    val newsErrorState = SingleLiveEvent<NewsErrorState>()

    private val cachedNewsObserver: Observer<List<NewsDO>> by lazy {
        Observer<List<NewsDO>> {
            val viewObjects = it.map { NewsVOMapper.mapFrom(from = it) }.sortedByDescending { it.date?.time }
            citizenNewsViewState.postValue(NewsViewState.NewsCacheLoadedViewState(news = viewObjects))
        }
    }

    private val touristNewsObserver: Observer<List<NewsDO>> by lazy {
        Observer<List<NewsDO>> {
            val viewObjects = it.map { NewsVOMapper.mapFrom(from = it) }.sortedByDescending { it.date?.time }
            touristNewsViewState.postValue(NewsViewState.TouristNewsLoaded(news = viewObjects))
        }
    }

    private var cachedNewsLiveData: LiveData<List<NewsDO>>? = null
    private var touristNews: LiveData<List<NewsDO>>? = null

    fun loadCachedNews() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedNewsLiveData = loadCachedNewsUseCase(Unit)
            launch(Dispatchers.Main) { cachedNewsLiveData?.observeForever(cachedNewsObserver) }
        }
    }

    fun loadNews(force: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadNewsUseCase(LoadNewsUseCase.Params(force))
            } catch (e: Exception) {
                newsErrorState.postValue(NewsErrorState.UnexpectedErrorEvent(e))
            }
        }
    }

    fun loadNewsForCity(city: CityInformationVO) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cityDom = CityInformationVOMapper.mapTo(to = city)
                val news = loadNewsForCityUseCase(LoadNewsForCityUseCase.Params(cityDom))
                val newsVo = news.map { NewsVOMapper.mapFrom(from = it) }
                citizenNewsViewState.postValue(NewsViewState.NewsLoadedViewState(newsVo))
            } catch (e: Exception) {
                newsErrorState.postValue(NewsErrorState.UnexpectedErrorEvent(e))
            }
        }
    }

    fun loadTouristNews(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            when (connectivityManager.isConnected()) {
                true -> {
                    touristNews = loadTouristNewsUseCase(LoadTouristNewsUseCase.Params(lat, lng))
                    launch(Dispatchers.Main) { touristNews?.observeForever(touristNewsObserver) }
                }
                else -> {
                    touristNewsViewState.postValue(NewsViewState.NoConnectionEvent())
                }
            }

        }
    }

    fun requestLocationPermission() {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            newsViewState.postValue(NewsViewState.LocationPermissionGranted())
        } else {
            newsViewState.postValue(NewsViewState.LocationPermissionNotGranted())
        }
    }

    fun locationPermissionDenied() {
        touristNewsViewState.postValue(NewsViewState.LocationPermissionNotGranted())
    }

    override fun onCleared() {
        super.onCleared()

        cachedNewsLiveData?.removeObserver(cachedNewsObserver)
        touristNews?.removeObserver(touristNewsObserver)
    }

}