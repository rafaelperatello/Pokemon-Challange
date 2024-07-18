package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.data.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataModule = module {

    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get(named(IO_CONTEXT)))
    }
}