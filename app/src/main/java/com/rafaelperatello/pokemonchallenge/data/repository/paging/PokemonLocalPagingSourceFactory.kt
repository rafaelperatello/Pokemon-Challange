package com.rafaelperatello.pokemonchallenge.data.repository.paging

import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import kotlin.coroutines.CoroutineContext

internal class PokemonLocalPagingSourceFactory(
    private val ioContext: CoroutineContext,
    private val pokemonDb: PokemonDatabase,
) {
    fun create(): PokemonLocalPagingSource =
        PokemonLocalPagingSource(
            ioContext = ioContext,
            pokemonDb = pokemonDb,
        )
}
