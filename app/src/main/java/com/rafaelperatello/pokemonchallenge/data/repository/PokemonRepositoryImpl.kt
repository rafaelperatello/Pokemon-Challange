package com.rafaelperatello.pokemonchallenge.data.repository

import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow.toShallowPokemonList
import com.rafaelperatello.pokemonchallenge.data.repository.remote.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemonList
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PokemonRepositoryImpl(
    private val pokemonService: PokemonApi,
    private val ioContext: CoroutineContext
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int): DomainResult<ShallowPokemonList> =
        withContext(ioContext) {
            safeApiCall({ dto -> dto.toShallowPokemonList() }) {
                pokemonService.getList(page)
            }
        }

    override suspend fun getPokemonDetails(id: String) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }
}
