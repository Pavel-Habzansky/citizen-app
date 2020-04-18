package com.pavelhabzansky.citizenapp.features.events.di

import com.pavelhabzansky.citizenapp.features.events.view.vm.EventDetailViewModel
import com.pavelhabzansky.citizenapp.features.events.view.vm.PushEventListViewModel
import com.pavelhabzansky.domain.features.events.usecase.DeletePushEventUseCase
import com.pavelhabzansky.domain.features.events.usecase.DownloadGalleryUseCase
import com.pavelhabzansky.domain.features.events.usecase.LoadInboxUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {

    viewModel { PushEventListViewModel() }

    viewModel { EventDetailViewModel() }

    single { DownloadGalleryUseCase(eventRepository = get()) }

    single { LoadInboxUseCase(eventsRepository = get()) }

    single { DeletePushEventUseCase(eventsRepository = get()) }

}