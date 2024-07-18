package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.data.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dataModule = module {

    singleOf(::PokemonRepositoryImpl) { bind<PokemonRepository>() }
}