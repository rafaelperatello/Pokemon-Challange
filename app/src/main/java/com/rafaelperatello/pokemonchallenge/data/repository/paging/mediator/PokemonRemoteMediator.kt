package com.rafaelperatello.pokemonchallenge.data.repository.paging.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import com.rafaelperatello.pokemonchallenge.data.repository.local.pojo.ShallowPokemonPojo
import com.rafaelperatello.pokemonchallenge.data.repository.remote.PokemonApi
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.toPokemonEntity
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.mapTo
import com.rafaelperatello.pokemonchallenge.data.repository.remote.util.safeApiCall
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalPagingApi::class)
internal class PokemonRemoteMediator(
    private val ioContext: CoroutineContext,
    private val pokemonService: PokemonApi,
    private val pokemonDao: PokemonDao,
) : RemoteMediator<Int, ShallowPokemonPojo>() {
    override suspend fun initialize(): InitializeAction =
        if (pokemonDao.getCount() == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ShallowPokemonPojo>,
    ): MediatorResult =
        withContext(ioContext) {
            Log.d("PokemonPaging", "MEDIATOR - loadType: $loadType, state: $state")

            val (page, pageSize) =
                when (loadType) {
                    LoadType.REFRESH -> {
                        // Todo use initial loading size
                        1 to state.config.initialLoadSize
                    }

                    LoadType.PREPEND -> {
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)
                        // 1 to state.config.pageSize Todo remove?
                    }

                    LoadType.APPEND -> {
                        val currentPage =
                            state.anchorPosition?.let { anchorPosition ->
                                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                            } ?: 0
//                    } ?: throw IllegalStateException("Anchor position is null") // Todo revert

                        currentPage + 1 to state.config.pageSize
                    }
                }

            val netWorkResult =
                safeApiCall(
                    dtoToEntityMapper = {
                        it.mapTo { dto -> dto.toPokemonEntity() }
                    },
                    apiCall = {
                        pokemonService.getCards(
                            currentPage = page,
                            pageSize = pageSize,
                        )
                    },
                )

            Log.d(
                "PokemonPaging",
                "MEDIATOR - page: $page, pageSize: $pageSize, netWorkResult: $netWorkResult",
            )

            when (netWorkResult) {
                is DomainResult.Success -> {
                    val data = netWorkResult.data.data
                    if (data.isEmpty()) {
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    }

                    pokemonDao.insertAllIgnoring(netWorkResult.data.data)

                    MediatorResult.Success(endOfPaginationReached = false)
                }

                is DomainResult.Error -> {
                    MediatorResult.Error(IllegalStateException("error: ${netWorkResult.error}"))
                }
            }
        }
}
