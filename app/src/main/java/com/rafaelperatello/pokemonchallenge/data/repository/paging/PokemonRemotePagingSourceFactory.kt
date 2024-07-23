package com.rafaelperatello.pokemonchallenge.data.repository.paging

import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import kotlin.coroutines.CoroutineContext

internal class PokemonRemotePagingSourceFactory(
    private val ioContext: CoroutineContext,
    private val pokemonApi: PokemonApi
) {

    fun create(): PokemonRemotePagingSource {
        return PokemonRemotePagingSource(
            ioContext = ioContext,
            pokemonApi = pokemonApi
        )
    }
}
