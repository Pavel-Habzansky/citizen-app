package com.pavelhabzansky.citizenapp.features.cities.detail.di

import com.pavelhabzansky.citizenapp.features.cities.detail.view.vm.CityDetailViewModel
import com.pavelhabzansky.domain.features.cities.usecase.GetResidentialCityNameUseCase
import com.pavelhabzansky.domain.features.cities.usecase.LoadCityInfoUseCase
import com.pavelhabzansky.domain.features.cities.usecase.SetCityResidentialForceUseCase
import com.pavelhabzansky.domain.features.cities.usecase.SetCityResidentialUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cityDetailModule = module {

    viewModel { CityDetailViewModel() }

    single { LoadCityInfoUseCase(cityRepository = get()) }

    single { SetCityResidentialUseCase(cityRepository = get()) }

    single { SetCityResidentialForceUseCase(cityRepository = get()) }

    single { GetResidentialCityNameUseCase(cityRepository = get()) }

}