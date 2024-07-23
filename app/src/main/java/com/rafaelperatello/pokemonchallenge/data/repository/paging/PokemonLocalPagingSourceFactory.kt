package com.rafaelperatello.pokemonchallenge.data.repository.paging

import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import kotlin.coroutines.CoroutineContext

internal class PokemonLocalPagingSourceFactory(
    private val ioContext: CoroutineContext,
    private val pokemonDb: PokemonDatabase,
) {

    fun create(): PokemonLocalPagingSource {
        return PokemonLocalPagingSource(
            ioContext = ioContext,
            pokemonDb = pokemonDb
        )
    }
}
