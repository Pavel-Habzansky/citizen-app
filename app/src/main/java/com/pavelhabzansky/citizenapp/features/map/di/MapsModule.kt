package com.pavelhabzansky.citizenapp.features.map.di

import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadBoundIssuesUseCase
import com.pavelhabzansky.domain.features.places.usecase.FetchPlacesUseCase
import com.pavelhabzansky.domain.features.places.usecase.LoadPlacesUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val MAPS_MODULE = "MAPS_MODULE"

val mapsModule = module {

    viewModel { MapViewModel(get()) }

    single(qualifier = named(MAPS_MODULE)) { FetchIssuesUseCase(get()) }

    single { LoadPlacesUseCase(get()) }

    single { FetchPlacesUseCase(get()) }

    single { LoadBoundIssuesUseCase(get()) }

}