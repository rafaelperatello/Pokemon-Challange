package com.rafaelperatello.pokemonchallenge.data.repository.paging.remoteonly

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.pojo.toShallowPokemon
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.mapTo
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.toPokemonEntity
import com.rafaelperatello.pokemonchallenge.data.repository.remote.util.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PokemonRemotePagingSource(
    private val pokemonApi: PokemonApi,
    private val ioContext: CoroutineContext,
    private val pokemonDb: PokemonDatabase,
) : PagingSource<Int, ShallowPokemon>() {
    override fun getRefreshKey(state: PagingState<Int, ShallowPokemon>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShallowPokemon> =
        withContext(ioContext) {
            delay(500) // Todo remove

            val page = params.key ?: 1
            val pageSize = params.loadSize
            val result =
                safeApiCall(
                    mapper = {
                        it.mapTo { dto -> dto.toPokemonEntity() }
                    },
                    apiCall = {
                        pokemonApi.getCards(page, pageSize)
                    },
                )

            Log.d(
                "PokemonPaging",
                "RemotePagingSource - page: $page, pageSize: ${pageSize}, result: $result",
            )

            when (result) {
                is DomainResult.Success -> {
                    val pokemonEntityList = result.data.data

                    val pokemonDao = pokemonDb.pokemonDao()
                    val affectedRows = pokemonDao.insertAllIgnoring(pokemonEntityList)

                    Log.d(
                        "PokemonPaging",
                        "RemotePagingSource - affectedRows: $affectedRows"
                    )

                    val all = pokemonDao.getAll()/// todo remove
                    val count = all.size/// todo remove

                    val offset = (page - 1) * pageSize
                    val shallowPokemonList =
                        pokemonDao.getAllShallow(
                            limit = pageSize,
                            offset = offset
                        ).map { it.toShallowPokemon() }

                    val nextKey =
                        when {
                            shallowPokemonList.isEmpty() -> null
                            shallowPokemonList.size < pageSize -> null
                            else -> page + 1
                        }

                    LoadResult.Page(
                        data = shallowPokemonList,
                        prevKey = params.key,
                        nextKey = nextKey,
                    )
                }

                is DomainResult.Error -> {
                    LoadResult.Error(IllegalStateException("error: ${result.error}"))
                }
            }
        }
}
