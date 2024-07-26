package com.rafaelperatello.pokemonchallenge.domain.settings

import kotlinx.coroutines.flow.Flow

internal interface AppSettings {
    // region Grid Style
    val gridStyle: Flow<GridStyle>
    suspend fun setGridStyle(gridStyle: GridStyle)
    // endregion

    // region Grid Style
    val appTheme: Flow<AppTheme>
    suspend fun setAppTheme(appTheme: AppTheme)
    // endregion
}

internal enum class GridStyle(
    val id: Int,
) {
    SMALL(0),
    MEDIUM(1),
    LARGE(2),
}

enum class AppTheme(
    val id: Int,
) {
    SYSTEM(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun fromId(id: Int) = entries.first { it.id == id }
    }
}
