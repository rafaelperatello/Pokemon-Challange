package com.rafaelperatello.pokemonchallenge.domain.settings

import kotlinx.coroutines.flow.Flow

internal interface AppSettings {

    // region Grid Style
    val gridStyle: Flow<GridStyle>

    suspend fun setGridStyle(gridStyle: GridStyle)

    suspend fun getGridStyle(): GridStyle
    // endregion

}

internal enum class GridStyle(val id: Int) {
    SMALL(0),
    MEDIUM(1),
    LARGE(2)
}
