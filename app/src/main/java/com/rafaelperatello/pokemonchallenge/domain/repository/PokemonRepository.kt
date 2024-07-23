package com.rafaelperatello.pokemonchallenge.domain.repository

import androidx.paging.PagingData
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.coroutines.flow.Flow

internal interface PokemonRepository {

    suspend fun getShallowPokemonList(): Flow<PagingData<ShallowPokemon>>
}
