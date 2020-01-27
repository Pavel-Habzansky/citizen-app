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
import com.pavelhabzansky.domain.features.cities.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import timber.log.Timber

class CityDetailViewModel : BaseViewModel() {

    private val loadCityInfoUseCase by inject<LoadCityInfoUseCase>()
    private val setCityResidentialUseCase by inject<SetCityResidentialUseCase>()
    private val setCityResidentialForceUseCase by inject<SetCityResidentialForceUseCase>()
    private val getResidentialCityNameUseCase by inject<GetResidentialCityNameUseCase>()
    private val getResidentialCityUseCase by inject<GetResidentialCityUseCase>()

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

    fun setCityAsResidential() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = setCityResidentialUseCase(
                params = SetCityResidentialUseCase.Params(
                    key = cityInfo.key,
                    name = cityInfo.name,
                    id = cityInfo.id
                )
            )

            when (result) {
                SetCityResidentialUseCase.UseCaseResult.SUCCESS -> {
                    Timber.i("City ${cityInfo.name}/${cityInfo.key} set as Residential")
                }
                else -> {
                    val currentResidential = getResidentialCityNameUseCase(Unit)
                    cityDetailViewState.postValue(CityDetailViewStates.ResidentialCityExists(name = currentResidential))
                }
            }
        }
    }

    fun setCityAsResidentialForce() {
        viewModelScope.launch(Dispatchers.IO) {
            setCityResidentialForceUseCase(
                params = SetCityResidentialForceUseCase.Params(
                    key = cityInfo.key,
                    name = cityInfo.name,
                    id = cityInfo.id
                )
            )
        }
    }

    fun loadResidentialCity() {
        viewModelScope.launch(Dispatchers.IO) {
            val residential = getResidentialCityUseCase(Unit)
            residential?.let {
                loadCityInfo(key = it.key)
            } ?: run {
                cityDetailViewState.postValue(CityDetailViewStates.NoResidentialCity())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        cityInfoLiveData?.removeObserver(cityInfoObserver)
    }

}