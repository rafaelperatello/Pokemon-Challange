package com.rafaelperatello.pokemonchallenge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rafaelperatello.pokemonchallenge.data.repository.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.data.settings.AppSettingsImpl
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

private const val DATA_STORE_PREFERENCES_NAME = "app_preferences"

internal val dataModule = module {

    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get(named(IO_CONTEXT)))
    }

    single<AppSettings> {
        AppSettingsImpl(get(), get(named(IO_CONTEXT)))
    }

    single<DataStore<Preferences>> {
        provideDataStore(androidContext(), get(named(IO_CONTEXT)))
    }
}

private fun provideDataStore(
    context: Context,
    ioContext: CoroutineContext
): DataStore<Preferences> {

    return PreferenceDataStoreFactory.create(
        scope = CoroutineScope(ioContext + SupervisorJob()),
        migrations = emptyList(),
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        )
    ) {
        context.preferencesDataStoreFile(DATA_STORE_PREFERENCES_NAME)
    }
}
