package com.rafaelperatello.pokemonchallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import com.rafaelperatello.pokemonchallenge.data.repository.paging.PagerConstants
import com.rafaelperatello.pokemonchallenge.data.repository.paging.mediator.PokemonLocalPagingSourceFactory
import com.rafaelperatello.pokemonchallenge.data.repository.paging.mediator.PokemonRemoteMediator
import com.rafaelperatello.pokemonchallenge.data.repository.paging.remoteonly.PokemonRemotePagingSourceFactory
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

internal class PokemonRepositoryImpl(
    private val pokemonLocalPagingSource: PokemonLocalPagingSourceFactory,
    private val pokemonRemoteMediator: PokemonRemoteMediator,
    private val pokemonDao: PokemonDao,
    private val pokemonRemotePagingSourceFactory: PokemonRemotePagingSourceFactory,
) : PokemonRepository {

    //    @OptIn(ExperimentalPagingApi::class)
//    override suspend fun getShallowPokemonList(): Flow<PagingData<ShallowPokemon>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                enablePlaceholders = true,
//                prefetchDistance = 10
//            ),
//            pagingSourceFactory = { pokemonLocalPagingSource.create() },
//            remoteMediator = pokemonRemoteMediator
//        ).flow
//    }

//    @OptIn(ExperimentalPagingApi::class)
//    override suspend fun getShallowPokemonList(): Flow<PagingData<ShallowPokemon>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                enablePlaceholders = true,
//                prefetchDistance = 10
//            ),
//            pagingSourceFactory = { pokemonDao.getAllPaging() },
//            remoteMediator = pokemonRemoteMediator
//        )
//            .flow
//            .map { pagingData ->
//                pagingData.map { it.toShallowPokemon() }
//            }
//    }

    override suspend fun getShallowPokemonList(): Flow<PagingData<ShallowPokemon>> =
        Pager(
            config =
            PagingConfig(
                pageSize = PagerConstants.PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = PagerConstants.PREFETCH_DISTANCE,
            ),
            pagingSourceFactory = { pokemonRemotePagingSourceFactory.create() },
        ).flow
}
