package com.pavelhabzansky.citizenapp.features.settings.di

import com.pavelhabzansky.citizenapp.features.settings.view.vm.SettingsViewModel
import com.pavelhabzansky.domain.features.settings.usecase.LoadAllSettingsUseCase
import com.pavelhabzansky.domain.features.settings.usecase.SaveSettingsUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel { SettingsViewModel() }

    single { LoadAllSettingsUseCase(get()) }

    single { SaveSettingsUseCase(get()) }

}