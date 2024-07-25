package com.rafaelperatello.pokemonchallenge.util

import android.content.Context
import android.util.Log
import com.rafaelperatello.pokemonchallenge.getHttpCacheDir
import com.rafaelperatello.pokemonchallenge.getImageCacheDir
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

private typealias ShouldFetchData = Boolean

internal interface SettingsUtil {
    val httpCacheSizeFlow: SharedFlow<Long>
    val imageCacheSizeFlow: SharedFlow<Long>

    suspend fun clearHttpCache(): Boolean
    suspend fun clearImageCache(): Boolean
}

internal class SettingsUtilImpl(
    ioContext: CoroutineContext,
    private val context: Context,
) : SettingsUtil, CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + ioContext

    private val _httpCacheSizeFlow: MutableSharedFlow<Long> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val httpCacheSizeFlow: SharedFlow<Long>
        get() = _httpCacheSizeFlow
            .asSharedFlow()
            .onSubscription {
                Log.d("SettingsUtilImpl", "httpCacheSizeMbFlow: onSubscription")
                fetchHttpCacheSizeStateFlow.tryEmit(true)
            }

    private val _imageCacheSizeFlow: MutableSharedFlow<Long> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val imageCacheSizeFlow: SharedFlow<Long>
        get() = _imageCacheSizeFlow
            .asSharedFlow()
            .onSubscription {
                Log.d("SettingsUtilImpl", "_imageCacheSizeMbFlow: onSubscription")
                fetchImageCacheSizeStateFlow.tryEmit(true)
            }

    private val fetchHttpCacheSizeStateFlow: MutableStateFlow<ShouldFetchData> =
        MutableStateFlow(false)
    private val fetchImageCacheSizeStateFlow: MutableStateFlow<ShouldFetchData> =
        MutableStateFlow(false)

    init {
        setInitialValue()
        fetchHttpCacheSizeOnFirstRequest()
        fetchImageCacheSizeOnFirstRequest()
    }

    private fun setInitialValue() {
        _httpCacheSizeFlow.tryEmit(0)
        _imageCacheSizeFlow.tryEmit(0)
    }

    private fun fetchHttpCacheSizeOnFirstRequest() {
        launch {
            fetchHttpCacheSizeStateFlow
                .first { it }
                .let {
                    Log.d("SettingsUtilImpl", "fetchHttpCacheSizeOnFirstRequest")
                    fetchHttpCacheSize()
                }
        }
    }

    private fun fetchImageCacheSizeOnFirstRequest() {
        launch {
            fetchImageCacheSizeStateFlow
                .first { it }
                .let {
                    Log.d("SettingsUtilImpl", "fetchImageCacheSizeOnFirstRequest")
                    fetchImageCacheSize()
                }
        }
    }

    override suspend fun clearHttpCache(): Boolean {
        return withContext(coroutineContext) {
            val success = context.getHttpCacheDir().deleteRecursively()
            if (success) {
                _httpCacheSizeFlow.tryEmit(0)
            }
            success
        }
    }

    override suspend fun clearImageCache(): Boolean {
        return withContext(coroutineContext) {
            val success = context.getImageCacheDir().deleteRecursively()
            if (success) {
                _imageCacheSizeFlow.tryEmit(0)
            }
            success
        }
    }

    private suspend fun fetchHttpCacheSize() {
        Log.d("SettingsUtilImpl", "fetchHttpCacheSize")
        withContext(coroutineContext) {
            val size = context.getHttpCacheDir().getSizeInKb()
            _httpCacheSizeFlow.tryEmit(size)
        }
    }

    private suspend fun fetchImageCacheSize() {
        Log.d("SettingsUtilImpl", "fetchImageCacheSize")
        withContext(coroutineContext) {
            val size = context.getImageCacheDir().getSizeInKb()
            _imageCacheSizeFlow.tryEmit(size)
        }
    }

    /**
     * Get the size of the directory in kilobytes
     */
    private fun File.getSizeInKb(): Long {
        return walkTopDown().sumOf { it.length() } / 1024
    }
}