package com.rafaelperatello.pokemonchallenge.data.repository.paging.remoteonly

import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import kotlin.coroutines.CoroutineContext

internal class PokemonRemotePagingSourceFactory(
    private val ioContext: CoroutineContext,
    private val pokemonApi: PokemonApi,
    private val pokemonDb: PokemonDatabase,
) {
    fun create(): PokemonRemotePagingSource =
        PokemonRemotePagingSource(
            ioContext = ioContext,
            pokemonApi = pokemonApi,
            pokemonDb = pokemonDb,
        )
}
