package com.pavelhabzansky.citizenapp.features.cities.detail.states

import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO

sealed class CityDetailViewStates {

    class CityInformationLoaded(val info: CityInformationVO) : CityDetailViewStates()

}

sealed class CityDetailErrorStates(val error: Throwable? = null) {


}