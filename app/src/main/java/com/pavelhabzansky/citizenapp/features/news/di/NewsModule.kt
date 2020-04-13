package com.pavelhabzansky.citizenapp.features.news.di

import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsDetailViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.domain.features.events.usecase.DownloadEventsUseCase
import com.pavelhabzansky.domain.features.events.usecase.DownloadImageUseCase
import com.pavelhabzansky.domain.features.events.usecase.LoadCachedEventsUseCase
import com.pavelhabzansky.domain.features.news.usecase.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {

    viewModel { NewsViewModel(get()) }

    viewModel { NewsDetailViewModel() }

    single { LoadNewsUseCase(newsRepository = get()) }

    single { LoadCachedNewsUseCase(newsRepository = get()) }

    single { LoadNewsItemUseCase(newsRepository = get()) }

    single { LoadNewsForCityUseCase(newsRepository =  get()) }

    single { LoadTouristNewsUseCase(newsRepository = get()) }

    single { DownloadEventsUseCase(eventsRepository = get()) }

    single { LoadCachedEventsUseCase(eventsRepository = get()) }

    single { DownloadImageUseCase(eventRepository = get()) }

}