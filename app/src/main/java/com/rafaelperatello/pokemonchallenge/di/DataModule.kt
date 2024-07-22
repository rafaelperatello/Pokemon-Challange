package com.rafaelperatello.pokemonchallenge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.rafaelperatello.pokemonchallenge.data.repository.PokemonRepositoryImpl
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabaseConstants
import com.rafaelperatello.pokemonchallenge.data.settings.AppSettingsImpl
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal object DataModuleConstants {

    const val DATA_STORE_PREFERENCES_NAME = "app_preferences"
}

internal val dataModule = module {

    single<PokemonRepository> {
        PokemonRepositoryImpl(
            pokemonService = get(),
            pokemonDb = get(),
            ioContext = get(named(IO_CONTEXT))
        )
    }

    single<AppSettings> {
        AppSettingsImpl(
            dataStore = get(),
            ioContext = get(named(IO_CONTEXT))
        )
    }

    single<DataStore<Preferences>> {
        provideDataStore(
            context = androidContext(),
            ioContext = get(named(IO_CONTEXT))
        )
    }

    single<PokemonDatabase> {
        provideDatabase(
            context = androidContext()
        )
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
        context.preferencesDataStoreFile(DataModuleConstants.DATA_STORE_PREFERENCES_NAME)
    }
}

private fun provideDatabase(
    context: Context
): PokemonDatabase {
    return Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        PokemonDatabaseConstants.DATABASE_NAME
    ).build()
}
