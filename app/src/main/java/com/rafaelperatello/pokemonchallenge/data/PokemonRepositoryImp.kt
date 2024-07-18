package com.rafaelperatello.pokemonchallenge.data

import com.rafaelperatello.pokemonchallenge.data.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.remote.dto.shallow.toShallowPokemonList
import com.rafaelperatello.pokemonchallenge.data.remote.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemonList
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class PokemonRepositoryImpl(
    private val pokemonService: PokemonApi
    // Todo Inject context
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int): DomainResult<ShallowPokemonList> =
        withContext(Dispatchers.IO) {
            // Todo disk cache
            safeApiCall({ dto -> dto.toShallowPokemonList() }) {
                pokemonService.getList(page)
            }
        }

    override suspend fun getPokemonDetails(id: String) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }
}
