package com.pavelhabzansky.citizenapp.features.issues.detail.di

import com.pavelhabzansky.citizenapp.features.issues.detail.view.vm.IssueDetailViewModel
import com.pavelhabzansky.domain.features.issues.usecase.LoadIssueImageUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val issueDetailModule = module {

    viewModel { IssueDetailViewModel() }

    single { LoadIssueImageUseCase(issuesRepository = get()) }

}