package com.rafaelperatello.pokemonchallenge.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.toMediumPokemon
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.toTypedDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PokemonRemotePagingSource(
    private val pokemonService: PokemonApi,
    private val ioContext: CoroutineContext
) : PagingSource<Int, ShallowPokemon>() {

    override fun getRefreshKey(state: PagingState<Int, ShallowPokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShallowPokemon> =
        withContext(ioContext) {
            val page = params.key ?: 1

            val result = safeApiCall(
                mapper = {
                    it.toTypedDTO { dto -> dto.toMediumPokemon() }
                },
                apiCall = {
                    pokemonService.getCardsMedium(page, params.loadSize)
                }
            )

            when (result) {
                is DomainResult.Success -> {
                    val data = result.data.data.map {
                        ShallowPokemon(
                            id = it.id,
                            name = it.name,
                            number = it.number,
                            imageSmall = it.imageSmall,
                            imageLarge = it.imageLarge
                        )
                    }
                    LoadResult.Page(
                        data = data,
                        prevKey = params.key,
                        nextKey = page + 1
                    )
                }

                is DomainResult.Error -> {
                    LoadResult.Error(
                        IllegalStateException("error: ${result.error}")
                    )
                }
            }
        }
}
