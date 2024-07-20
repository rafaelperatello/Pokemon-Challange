package com.rafaelperatello.pokemonchallenge.domain.settings

import kotlinx.coroutines.flow.SharedFlow

internal interface AppSettings {

    // region Grid Style
    val gridStyle: SharedFlow<GridStyle>

    suspend fun setGridStyle(gridStyle: GridStyle)

    suspend fun getGridStyle(): GridStyle
    // endregion

}

internal enum class GridStyle {
    SMALL,
    MEDIUM,
    LARGE
}
