package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.IO_CONTEXT
import com.rafaelperatello.pokemonchallenge.ui.screen.home.HomeViewModel
import com.rafaelperatello.pokemonchallenge.ui.screen.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val ViewModule =
    module {

        viewModelOf(::HomeViewModel)

        viewModel() {
            SettingsViewModel(
                settingsUtil = get(),
                pokemonDb = get(),
                ioDispatcher = get(named(IO_CONTEXT))
            )
        }
    }
