package com.rafaelperatello.pokemonchallenge.data.repository.paging

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.paging.util.ThreadSafeInvalidationObserver
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabaseConstants
import com.rafaelperatello.pokemonchallenge.data.repository.local.pojo.toShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@SuppressLint("RestrictedApi")
internal class PokemonLocalPagingSource(
    private val ioContext: CoroutineContext,
    private val pokemonDb: PokemonDatabase,
) : PagingSource<Int, ShallowPokemon>() {

    private val observer = ThreadSafeInvalidationObserver(
        tables = arrayOf(PokemonDatabaseConstants.Tables.POKEMON),
        onInvalidated = {
            invalidate()
            Log.d("PokemonPaging", "SOURCE - invalidate")
        }
    )

    override fun getRefreshKey(state: PagingState<Int, ShallowPokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShallowPokemon> =
        withContext(ioContext) {
            observer.registerIfNecessary(pokemonDb)

            val page = params.key ?: 1
            val offset = (page - 1) * params.loadSize // 0 based offset

            Log.d("PokemonPaging", "SOURCE - load: $page, params: $params")

            val data = pokemonDb.pokemonDao().getAllShallow(
                limit = params.loadSize,
                offset = offset
            ).map {
                it.toShallowPokemon()
            }


            val prevKey = if (page == 1) null else page - 1
            val nextKey =
                when {
                    data.isEmpty() -> null
                    page == 1 -> (data.size / PagerConstants.PAGE_SIZE) + 1
                    else -> page + 1
                }

            Log.d(
                "PokemonPaging",
                "SOURCE - data: ${data.size}, page: $page, prevKey: $prevKey, nextKey: $nextKey"
            )

            LoadResult.Page(data, prevKey, nextKey)
        }
}
