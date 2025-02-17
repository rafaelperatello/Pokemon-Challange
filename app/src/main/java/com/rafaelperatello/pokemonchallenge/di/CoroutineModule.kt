package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.CPU_CONTEXT
import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.IO_CONTEXT
import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.MAIN_CONTEXT
import com.rafaelperatello.pokemonchallenge.ui.screen.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal object CoroutineConstants {
    const val IO_CONTEXT = "IO_DISPATCHER"
    const val CPU_CONTEXT = "CPU_DISPATCHER"
    const val MAIN_CONTEXT = "MAIN_DISPATCHER"
}

internal val CoroutineModule =
    module {

        viewModelOf(::HomeViewModel)

        single<CoroutineContext>(named(IO_CONTEXT)) { Dispatchers.IO }

        single<CoroutineContext>(named(CPU_CONTEXT)) { Dispatchers.Default }

        single<CoroutineContext>(named(MAIN_CONTEXT)) { Dispatchers.Main }
    }
