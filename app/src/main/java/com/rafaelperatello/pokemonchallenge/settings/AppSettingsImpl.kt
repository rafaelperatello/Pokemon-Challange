package com.rafaelperatello.pokemonchallenge.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import com.rafaelperatello.pokemonchallenge.domain.settings.AppTheme
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AppSettingsImpl(
    private val dataStore: DataStore<Preferences>,
    private val ioContext: CoroutineContext,
) : AppSettings {
    companion object {
        private val keyGridStyle = intPreferencesKey("keyGridStyle")
        private val keyAppTheme = intPreferencesKey("keyAppTheme")
    }

    // region Grid Style
    override val gridStyle: Flow<GridStyle>
        get() = dataStore.data
            .map { it[keyGridStyle] ?: GridStyle.MEDIUM.id }
            .map { id -> GridStyle.entries.first { it.id == id } }
            .distinctUntilChanged()

    override suspend fun setGridStyle(gridStyle: GridStyle): Unit =
        withContext(ioContext) {
            dataStore.edit { preferences ->
                preferences[keyGridStyle] = gridStyle.id
            }
        }
    // endregion

    // region App Theme
    override val appTheme: Flow<AppTheme>
        get() = dataStore.data
            .map { it[keyAppTheme] ?: AppTheme.SYSTEM.id }
            .map { id -> AppTheme.fromId(id) }
            .distinctUntilChanged()


    override suspend fun setAppTheme(appTheme: AppTheme) {
        withContext(ioContext) {
            dataStore.edit { preferences ->
                preferences[keyAppTheme] = appTheme.id
            }
        }
    }
    // endregion
}
