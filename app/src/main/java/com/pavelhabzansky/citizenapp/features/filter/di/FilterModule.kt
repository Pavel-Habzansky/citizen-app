package com.pavelhabzansky.citizenapp.features.filter.di

import com.pavelhabzansky.citizenapp.features.filter.view.vm.FilterViewModel
import com.pavelhabzansky.domain.features.events.usecase.GetSettingsUseCase
import com.pavelhabzansky.domain.features.events.usecase.SaveFilterUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {

    viewModel { FilterViewModel() }

    single { GetSettingsUseCase(eventRepository = get()) }

    single { SaveFilterUseCase(eventsRepository = get()) }

}