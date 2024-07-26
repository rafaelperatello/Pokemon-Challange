package com.rafaelperatello.pokemonchallenge.data.repository.paging.remoteonly

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import com.rafaelperatello.pokemonchallenge.data.repository.local.pojo.toShallowPokemon
import com.rafaelperatello.pokemonchallenge.data.repository.paging.PagerConstants
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.mapTo
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.toPokemonEntity
import com.rafaelperatello.pokemonchallenge.data.repository.remote.util.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PokemonUnifiedPagingSource(
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
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val offset = (page - 1) * pageSize

            // Check presence in local db
            val pokemonDao = pokemonDb.pokemonDao()
            val shallowPokemonList = pokemonDao.getShallowPokemonList(pageSize, offset)

            if (shallowPokemonList.size == pageSize) {
                val nextKey = getNextKey(page, pageSize)

                Log.d(
                    "PokemonPaging",
                    "LocalPagingSource - DB hit - page: $page, pageSize: ${pageSize}, nextKey: $nextKey"
                )
                return@withContext LoadResult.Page(
                    data = shallowPokemonList,
                    prevKey = params.key,
                    nextKey = nextKey,
                )
            }

            // Fetch from remote
            val result =
                safeApiCall(
                    dtoToEntityMapper = {
                        it.mapTo { dto -> dto.toPokemonEntity() }
                    },
                    apiCall = {
                        pokemonApi.getCards(page, pageSize)
                    },
                )

            Log.d(
                "PokemonPaging",
                "RemotePagingSource - page: $page, pageSize: $pageSize, result: $result",
            )

            when (result) {
                is DomainResult.Success -> {
                    val pokemonEntityList = result.data.data

                    val affectedRows = pokemonDao.insertAllIgnoring(pokemonEntityList)

                    Log.d(
                        "PokemonPaging",
                        "RemotePagingSource - affectedRows: $affectedRows"
                    )

                    val freshShallowPokemonList = pokemonDao.getShallowPokemonList(pageSize, offset)

                    val nextKey =
                        when {
                            freshShallowPokemonList.isEmpty() -> null
                            freshShallowPokemonList.size < pageSize -> null
                            else -> getNextKey(page, pageSize)
                        }

                    LoadResult.Page(
                        data = freshShallowPokemonList,
                        prevKey = params.key,
                        nextKey = nextKey,
                    )
                }

                is DomainResult.Error -> {
                    LoadResult.Error(IllegalStateException("error: ${result.error}"))
                }
            }
        }

    /**
     * Initial load size is by default bigger than the default page size, so we need to
     * divide it by the default page size to get the correct next key
     */
    private fun getNextKey(
        page: Int,
        pageSize: Int,
    ) = page + pageSize / PagerConstants.PAGE_SIZE

    private suspend fun PokemonDao.getShallowPokemonList(
        pageSize: Int,
        offset: Int,
    ) =
        this
            .getAllShallow(
                limit = pageSize,
                offset = offset
            ).map {
                it.toShallowPokemon()
            }
}
