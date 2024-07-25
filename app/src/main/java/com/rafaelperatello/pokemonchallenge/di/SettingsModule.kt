package com.rafaelperatello.pokemonchallenge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rafaelperatello.pokemonchallenge.SettingsModuleConstants
import com.rafaelperatello.pokemonchallenge.di.CoroutineConstants.IO_CONTEXT
import com.rafaelperatello.pokemonchallenge.settings.AppSettingsImpl
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal val SettingsModule =
    module {

        single<AppSettings> {
            AppSettingsImpl(
                dataStore = get(),
                ioContext = get(named(IO_CONTEXT)),
            )
        }

        single<DataStore<Preferences>> {
            provideDataStore(
                context = androidContext(),
                ioContext = get(named(IO_CONTEXT)),
            )
        }
    }

private fun provideDataStore(
    context: Context,
    ioContext: CoroutineContext,
): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        scope = CoroutineScope(ioContext + SupervisorJob()),
        migrations = emptyList(),
        corruptionHandler =
        ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
    ) {
        context.preferencesDataStoreFile(SettingsModuleConstants.DATA_STORE_PREFERENCES_NAME)
    }
