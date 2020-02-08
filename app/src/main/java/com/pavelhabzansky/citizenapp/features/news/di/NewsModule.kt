package com.pavelhabzansky.citizenapp.features.news.di

import com.pavelhabzansky.citizenapp.features.news.view.vm.NewsViewModel
import com.pavelhabzansky.domain.features.news.usecase.ConnectFirebaseUseCase
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {

    viewModel { NewsViewModel() }

    single {
        ConnectFirebaseUseCase(
            newsRepository = get()
        )
    }

    single {
        LoadNewsUseCase(
            newsRepository = get()
        )
    }

}