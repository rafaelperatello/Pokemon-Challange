package com.rafaelperatello.pokemonchallenge.domain.repository

import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemonList
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult

internal interface PokemonRepository {

    suspend fun getPokemonList(page: Int): DomainResult<ShallowPokemonList>

    // Todo full entity
    suspend fun getPokemonDetails(id: String): DomainResult<Unit>
}
