package com.pavelhabzansky.citizenapp.features.issues.list.di

import com.pavelhabzansky.citizenapp.features.issues.list.view.vm.MapListViewModel
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadMapItemsUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ISSUES_LIST_MODULE = "ISSUES_LIST_MODULE"

val issueListModule = module {

    viewModel { MapListViewModel() }

    single { LoadMapItemsUseCase(get()) }

    single(qualifier = named(ISSUES_LIST_MODULE)) { FetchIssuesUseCase(get()) }

}