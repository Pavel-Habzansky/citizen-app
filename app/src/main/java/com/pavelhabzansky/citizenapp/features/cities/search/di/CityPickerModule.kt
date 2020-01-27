package com.pavelhabzansky.citizenapp.features.cities.search.di

import com.pavelhabzansky.citizenapp.features.cities.search.view.vm.CityPickerViewModel
import com.pavelhabzansky.domain.features.cities.usecase.LoadFilteredCitiesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.LoadLastSearchesUseCase
import com.pavelhabzansky.domain.features.cities.usecase.SaveSearchedItemUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cityPickerModule = module {

    viewModel { CityPickerViewModel() }

    single { LoadLastSearchesUseCase(cityRepository = get()) }

    single { LoadFilteredCitiesUseCase(cityRepository = get()) }

    single { SaveSearchedItemUseCase(cityRepository = get()) }

}