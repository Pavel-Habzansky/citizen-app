package com.pavelhabzansky.citizenapp.features.place.di

import com.pavelhabzansky.citizenapp.features.place.view.vm.PlaceDetailViewModel
import com.pavelhabzansky.domain.features.places.usecase.LoadPlaceImageUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val placesModule = module {

    viewModel { PlaceDetailViewModel() }

    single { LoadPlaceImageUseCase(get()) }

}