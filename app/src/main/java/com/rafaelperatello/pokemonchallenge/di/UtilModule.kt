package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.IO_CONTEXT
import com.rafaelperatello.pokemonchallenge.util.SettingsUtil
import com.rafaelperatello.pokemonchallenge.util.SettingsUtilImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val UtilModule =
    module {

        single<SettingsUtil> {
            SettingsUtilImpl(
                ioContext = get(named(IO_CONTEXT)),
                context = androidContext(),
            )
        }
    }
