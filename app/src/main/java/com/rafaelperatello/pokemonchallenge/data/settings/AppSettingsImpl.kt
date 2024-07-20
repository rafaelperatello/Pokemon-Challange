package com.rafaelperatello.pokemonchallenge.data.settings

import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AppSettingsImpl(
    private val ioContext: CoroutineContext
) : AppSettings {

    private val mutableGridStyle: MutableStateFlow<GridStyle> = MutableStateFlow(GridStyle.MEDIUM)

    override val gridStyle: SharedFlow<GridStyle> = mutableGridStyle.asSharedFlow()

    override suspend fun setGridStyle(gridStyle: GridStyle) = withContext(ioContext) {
        mutableGridStyle.value = gridStyle
    }

    override suspend fun getGridStyle(): GridStyle = withContext(ioContext) {
        mutableGridStyle.value
    }
}