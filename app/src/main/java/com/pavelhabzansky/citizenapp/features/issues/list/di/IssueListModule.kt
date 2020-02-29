package com.pavelhabzansky.citizenapp.features.issues.list.di

import com.pavelhabzansky.citizenapp.features.issues.list.view.vm.IssueListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val issueListModule = module {

    viewModel { IssueListViewModel() }

}