package com.pavelhabzansky.citizenapp.features.events.di

import com.pavelhabzansky.citizenapp.features.events.view.vm.EventDetailViewModel
import com.pavelhabzansky.domain.features.events.usecase.DownloadGalleryUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {

    viewModel { EventDetailViewModel() }

    single { DownloadGalleryUseCase(eventRepository = get()) }

}