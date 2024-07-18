package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {

    viewModelOf(::MainViewModel)
}