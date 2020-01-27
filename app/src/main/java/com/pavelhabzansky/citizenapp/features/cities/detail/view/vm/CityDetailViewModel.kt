package com.pavelhabzansky.citizenapp.features.cities.detail.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailErrorStates
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailViewStates
import com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper.CityInformationVOMapper
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.usecase.LoadCityInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class CityDetailViewModel : BaseViewModel() {

    private val loadCityInfoUseCase by inject<LoadCityInfoUseCase>()

    private var cityInfoLiveData: LiveData<CityInformationDO>? = null

    private val cityInfoObserver: Observer<CityInformationDO> by lazy {
        Observer<CityInformationDO> {
            cityInfo = CityInformationVOMapper.mapFrom(from = it)
            cityDetailViewState.value = CityDetailViewStates.CityInformationLoaded(cityInfo)
        }
    }

    val cityDetailViewState = SingleLiveEvent<CityDetailViewStates>()
    val cityDetailErrorState = SingleLiveEvent<CityDetailErrorStates>()

    lateinit var cityInfo: CityInformationVO

    fun loadCityInfo(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cityInfoLiveData = loadCityInfoUseCase(params = key)
            launch(Dispatchers.Main) { cityInfoLiveData?.observeForever(cityInfoObserver) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        cityInfoLiveData?.removeObserver(cityInfoObserver)
    }

}