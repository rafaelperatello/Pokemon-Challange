package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.IO_CONTEXT
import com.rafaelperatello.pokemonchallenge.util.CacheUtil
import com.rafaelperatello.pokemonchallenge.util.CacheUtilImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val UtilModule =
    module {

        single<CacheUtil> {
            CacheUtilImpl(
                ioContext = get(named(IO_CONTEXT)),
                context = androidContext(),
            )
        }
    }
