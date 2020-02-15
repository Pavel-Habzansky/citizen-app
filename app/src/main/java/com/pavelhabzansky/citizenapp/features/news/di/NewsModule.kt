package com.pavelhabzansky.citizenapp.features.news.di

import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsDetailViewModel
import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.domain.features.news.usecase.LoadCachedNewsUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsItemUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {

    viewModel { NewsViewModel() }

    viewModel { NewsDetailViewModel() }

    single { LoadNewsUseCase(newsRepository = get()) }

    single { LoadCachedNewsUseCase(newsRepository = get()) }

    single { LoadNewsItemUseCase(newsRepository = get()) }

}