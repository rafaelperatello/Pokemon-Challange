package com.rafaelperatello.pokemonchallenge.di

import android.content.Context
import androidx.room.Room
import com.rafaelperatello.pokemonchallenge.data.repository.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabaseConstants
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import com.rafaelperatello.pokemonchallenge.data.repository.paging.mediator.PokemonLocalPagingSourceFactory
import com.rafaelperatello.pokemonchallenge.data.repository.paging.mediator.PokemonRemoteMediator
import com.rafaelperatello.pokemonchallenge.data.repository.paging.remoteonly.PokemonRemotePagingSourceFactory
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val DataModule =
    module {

        single<PokemonRepository> {
            PokemonRepositoryImpl(
                pokemonLocalPagingSource = get(),
                pokemonRemoteMediator = get(),
                pokemonDao = get(),
                pokemonRemotePagingSourceFactory = get(),
            )
        }

        single<PokemonDatabase> {
            provideDatabase(
                context = androidContext(),
            )
        }

        single<PokemonDao> {
            get<PokemonDatabase>().pokemonDao()
        }

        single {
            PokemonRemotePagingSourceFactory(
                ioContext = get(named(IO_CONTEXT)),
                pokemonApi = get(),
                pokemonDb = get(),
            )
        }

        single {
            PokemonLocalPagingSourceFactory(
                ioContext = get(named(IO_CONTEXT)),
                pokemonDb = get(),
            )
        }

        single {
            PokemonRemoteMediator(
                ioContext = get(named(IO_CONTEXT)),
                pokemonService = get(),
                pokemonDao = get(),
            )
        }
    }

private fun provideDatabase(context: Context): PokemonDatabase =
    Room
        .databaseBuilder(
            context,
            PokemonDatabase::class.java,
            PokemonDatabaseConstants.DATABASE_NAME,
        ).build()
