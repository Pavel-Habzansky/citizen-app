package com.pavelhabzansky.citizenapp.features.issues.myissues.di

import com.pavelhabzansky.citizenapp.features.issues.myissues.view.vm.MyIssuesViewModel
import com.pavelhabzansky.domain.features.issues.usecase.LoadUserIssuesUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myIssuesModule = module {

    viewModel { MyIssuesViewModel() }

    single { LoadUserIssuesUseCase(get()) }

}