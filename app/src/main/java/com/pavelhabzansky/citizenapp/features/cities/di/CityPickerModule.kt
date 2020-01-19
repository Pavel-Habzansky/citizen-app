package com.pavelhabzansky.citizenapp.features.cities.di

import com.pavelhabzansky.citizenapp.features.cities.view.vm.CityPickerViewModel
import com.pavelhabzansky.domain.features.cities.usecase.LoadFilteredCitiesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.LoadLastSearchesUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cityPickerModule = module {

    viewModel { CityPickerViewModel() }

    single {
        LoadLastSearchesUseCase(
            cityRepository = get()
        )
    }

    single {
        LoadFilteredCitiesUseCase(
            cityRepository = get()
        )
    }

}