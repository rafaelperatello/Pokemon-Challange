package com.rafaelperatello.pokemonchallenge.di

import com.rafaelperatello.pokemonchallenge.ui.screen.home.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

const val IO_CONTEXT = "IO_DISPATCHER"
const val CPU_CONTEXT = "CPU_DISPATCHER"
const val MAIN_CONTEXT = "MAIN_DISPATCHER"

internal val appModule = module {

    viewModelOf(::MainViewModel)

    single<CoroutineContext>(named(IO_CONTEXT)) { Dispatchers.IO }

    single<CoroutineContext>(named(CPU_CONTEXT)) { Dispatchers.Default }

    single<CoroutineContext>(named(MAIN_CONTEXT)) { Dispatchers.Main }
}
