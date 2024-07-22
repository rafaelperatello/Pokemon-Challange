package com.rafaelperatello.pokemonchallenge.data.repository

import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.toMediumPokemon
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.toTypedDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonList
import com.rafaelperatello.pokemonchallenge.domain.model.medium.MediumPokemon
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PokemonRepositoryImpl(
    private val pokemonService: PokemonApi,
    private val pokemonDb: PokemonDatabase,
    private val ioContext: CoroutineContext
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int): DomainResult<PokemonList<ShallowPokemon>> =
        withContext(ioContext) {
            when (val result = getPokemonListMedium(page)) {
                is DomainResult.Success -> DomainResult.Success(
                    PokemonList(
                        data = result.data.data.map {
                            ShallowPokemon(
                                id = it.id,
                                name = it.name,
                                number = it.number,
                                imageSmall = it.imageSmall,
                                imageLarge = it.imageLarge
                            )
                        },
                        page = result.data.page,
                        pageSize = result.data.pageSize,
                        count = result.data.count,
                        totalCount = result.data.totalCount
                    )
                )

                is DomainResult.Error -> DomainResult.Error(result.error)
            }
        }

    override suspend fun getPokemonListMedium(page: Int): DomainResult<PokemonList<MediumPokemon>> =
        withContext(Dispatchers.IO) {
            safeApiCall(
                mapper = {
                    it.toTypedDTO { dto -> dto.toMediumPokemon() }
                },
                apiCall = {
                    pokemonService.getCardsMedium(page)
                }
            )

        }

    override suspend fun getPokemonDetails(id: String) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }
}
