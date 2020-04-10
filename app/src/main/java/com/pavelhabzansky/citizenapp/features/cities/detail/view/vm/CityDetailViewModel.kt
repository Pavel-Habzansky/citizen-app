package com.pavelhabzansky.citizenapp.features.cities.detail.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailErrorStates
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailViewStates
import com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper.CityInformationVOMapper
import com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper.GalleryVOMapper
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityGalleryItemVO
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.domain.features.cities.domain.CityGalleryItemDO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import timber.log.Timber

class CityDetailViewModel : BaseViewModel() {

    private val loadCityInfo by inject<LoadCityInfoUseCase>()
    private val setCityResidential by inject<SetCityResidentialUseCase>()
    private val setCityResidentialForce by inject<SetCityResidentialForceUseCase>()
    private val getResidentialCityName by inject<GetResidentialCityNameUseCase>()
    private val getResidentialCity by inject<GetResidentialCityUseCase>()
    private val getPhotogallery by inject<GetPhotogalleryUseCase>()

    private var cityInfoLiveData: LiveData<CityInformationDO>? = null
    private var galleryLiveData: LiveData<List<CityGalleryItemDO>>? = null

    private val cityInfoObserver: Observer<CityInformationDO> by lazy {
        Observer<CityInformationDO> {
            cityInfo = CityInformationVOMapper.mapFrom(from = it)
            cityDetailViewState.value = CityDetailViewStates.CityInformationLoaded(cityInfo)
        }
    }

    private val galleryObserver: Observer<List<CityGalleryItemDO>> by lazy {
        Observer<List<CityGalleryItemDO>> {
            val gallery = it.map { GalleryVOMapper.mapFrom(it) }
            cityDetailViewState.value = CityDetailViewStates.CityGalleryLoadedEvent(gallery)
        }
    }

    val cityDetailViewState = SingleLiveEvent<CityDetailViewStates>()
    val cityDetailErrorState = SingleLiveEvent<CityDetailErrorStates>()

    lateinit var cityInfo: CityInformationVO

    fun loadCityInfo(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cityInfoLiveData = loadCityInfo(params = key)
            launch(Dispatchers.Main) { cityInfoLiveData?.observeForever(cityInfoObserver) }

            galleryLiveData = getPhotogallery(key)
            launch(Dispatchers.Main) {galleryLiveData?.observeForever(galleryObserver)}
        }
    }

    fun setCityAsResidential() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = setCityResidential(
                    params = SetCityResidentialUseCase.Params(
                            city = CityInformationVOMapper.mapTo(to = cityInfo)
                    )
            )

            when (result) {
                SetCityResidentialUseCase.UseCaseResult.SUCCESS -> {
                    Timber.i("City ${cityInfo.name}/${cityInfo.key} set as Residential")
                    cityDetailViewState.postValue(CityDetailViewStates.SetResidential())
                }
                else -> {
                    val currentResidential = getResidentialCityName(Unit)
                    cityDetailViewState.postValue(CityDetailViewStates.ResidentialCityExists(name = currentResidential))
                }
            }
        }
    }

    fun setCityAsResidentialForce() {
        viewModelScope.launch(Dispatchers.IO) {
            setCityResidentialForce(
                    params = SetCityResidentialForceUseCase.Params(
                            city = CityInformationVOMapper.mapTo(to = cityInfo)
                    )
            )
            cityDetailViewState.postValue(CityDetailViewStates.SetResidential())
        }
    }

    fun loadResidentialCity() {
        viewModelScope.launch(Dispatchers.IO) {
            val residential = getResidentialCity(Unit)
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
        galleryLiveData?.removeObserver(galleryObserver)
    }

}