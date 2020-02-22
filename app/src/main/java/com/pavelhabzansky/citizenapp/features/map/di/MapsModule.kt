package com.pavelhabzansky.citizenapp.features.map.di

import com.pavelhabzansky.citizenapp.features.map.view.vm.MapViewModel
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadLiveIssuesUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapsModule = module {

    viewModel { MapViewModel(get()) }

    single { FetchIssuesUseCase(get()) }

    single { LoadLiveIssuesUseCase(get()) }

}