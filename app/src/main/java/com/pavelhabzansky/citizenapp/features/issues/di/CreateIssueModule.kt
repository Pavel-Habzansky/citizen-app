package com.pavelhabzansky.citizenapp.features.issues.di

import com.pavelhabzansky.citizenapp.features.issues.view.vm.CreateIssueViewModel
import com.pavelhabzansky.domain.features.issues.usecase.CreateIssueUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createIssueModule = module {

    viewModel { CreateIssueViewModel(get()) }

    single { CreateIssueUseCase(get()) }

}