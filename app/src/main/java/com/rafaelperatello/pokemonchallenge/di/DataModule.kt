package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.data.repository.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.data.settings.AppSettingsImpl
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataModule = module {

    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get(named(IO_CONTEXT)))
    }

    single<AppSettings> {
        AppSettingsImpl(get(named(IO_CONTEXT)))
    }
}