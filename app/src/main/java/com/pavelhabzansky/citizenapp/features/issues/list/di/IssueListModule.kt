package com.pavelhabzansky.citizenapp.features.issues.list.di

import com.pavelhabzansky.citizenapp.features.issues.list.view.vm.IssueListViewModel
import com.pavelhabzansky.domain.features.issues.usecase.FetchIssuesUseCase
import com.pavelhabzansky.domain.features.issues.usecase.LoadIssuesUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ISSUES_LIST_MODULE = "ISSUES_LIST_MODULE"

val issueListModule = module {

    viewModel { IssueListViewModel() }

    single { LoadIssuesUseCase(get()) }

    single(qualifier = named(ISSUES_LIST_MODULE)) { FetchIssuesUseCase(get()) }

}