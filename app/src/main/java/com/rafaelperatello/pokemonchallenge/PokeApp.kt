package com.rafaelperatello.pokemonchallenge

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.rafaelperatello.pokemonchallenge.di.AppModule
import com.rafaelperatello.pokemonchallenge.di.DataModule
import com.rafaelperatello.pokemonchallenge.di.NetworkModule
import com.rafaelperatello.pokemonchallenge.di.SettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class PokeApp :
    Application(),
    ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@PokeApp)
            modules(
                AppModule,
                DataModule,
                NetworkModule,
                SettingsModule,
            )
        }
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader
            .Builder(this)
            .memoryCache {
                MemoryCache
                    .Builder(this)
                    .maxSizePercent(ImageConstants.Cache.MEMORY_PERCENT)
                    .build()
            }.diskCache {
                DiskCache
                    .Builder()
                    .directory(cacheDir.resolve(ImageConstants.Cache.DIRECTORY))
                    .maxSizeBytes(ImageConstants.Cache.DISK_SIZE)
                    .build()
            }.logger(DebugLogger(level = Log.INFO))
            .respectCacheHeaders(false)
            .build()
}
