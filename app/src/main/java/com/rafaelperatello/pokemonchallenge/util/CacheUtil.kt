package com.rafaelperatello.pokemonchallenge.util

import android.content.Context
import android.util.Log
import com.rafaelperatello.pokemonchallenge.getHttpCacheDir
import com.rafaelperatello.pokemonchallenge.getImageCacheDir
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

private enum class FileObserverAction {
    REFRESH,
    SKIP,
}

internal interface CacheUtil {

    val httpCacheSizeFlow: SharedFlow<Long>

    val imageCacheSizeFlow: SharedFlow<Long>

    suspend fun clearHttpCache(): Boolean

    suspend fun clearImageCache(): Boolean
}

internal class CacheUtilImpl(
    ioContext: CoroutineContext,
    private val context: Context,
) : CacheUtil,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + ioContext

    private val _httpCacheSizeFlow: MutableSharedFlow<Long> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    override val httpCacheSizeFlow: SharedFlow<Long>
        get() = _httpCacheSizeFlow.asSharedFlow()

    private val _imageCacheSizeFlow: MutableSharedFlow<Long> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    override val imageCacheSizeFlow: SharedFlow<Long>
        get() = _imageCacheSizeFlow.asSharedFlow()

    private val httpFileObserver = CacheFileObserver(context.getHttpCacheDir(), coroutineContext)

    private val imageFileObserver = CacheFileObserver(context.getImageCacheDir(), coroutineContext)

    init {
        observerCacheFolder(
            subscriberOwner = _httpCacheSizeFlow,
            fileObserver = httpFileObserver,
        ) {
            refreshHttpCacheSize()
        }

        observerCacheFolder(
            subscriberOwner = _imageCacheSizeFlow,
            fileObserver = imageFileObserver,
        ) {
            refreshImageCacheSize()
        }
    }

    private fun <T> observerCacheFolder(
        subscriberOwner: MutableSharedFlow<T>,
        fileObserver: CacheFileObserver,
        onRefreshData: suspend () -> Unit,
    ) {
        launch {
            subscriberOwner
                .subscriptionCount
                .map { subscriberCount ->
                    Log.d(
                        "SettingsUtilImpl",
                        "${fileObserver.id}: subscriptionCount: $subscriberCount"
                    )
                    if (subscriberCount > 0) {
                        fileObserver
                            .dataChangedFlow
                            .map { FileObserverAction.REFRESH }
                    } else {
                        flowOf(FileObserverAction.SKIP)
                    }
                }.collectLatest { fileObserverActionFlow ->
                    fileObserverActionFlow
                        .filter { it == FileObserverAction.REFRESH }
                        .onCompletion {
                            Log.d(
                                "SettingsUtilImpl",
                                "${fileObserver.id} onCompletion"
                            )
                        }.collect {
                            Log.d("SettingsUtilImpl", "${fileObserver.id} collected $it")
                            onRefreshData()
                        }
                }
        }
    }

    override suspend fun clearHttpCache(): Boolean = withContext(coroutineContext) {
        context.getHttpCacheDir().deleteRecursively()
    }

    override suspend fun clearImageCache(): Boolean = withContext(coroutineContext) {
        context.getImageCacheDir().deleteRecursively()
    }

    private suspend fun refreshHttpCacheSize() {
        Log.d("SettingsUtilImpl", "fetchHttpCacheSize")
        withContext(coroutineContext) {
            val size = context.getHttpCacheDir().getSizeInKb()
            _httpCacheSizeFlow.tryEmit(size)
        }
    }

    private suspend fun refreshImageCacheSize() {
        Log.d("SettingsUtilImpl", "fetchImageCacheSize")
        withContext(coroutineContext) {
            val size = context.getImageCacheDir().getSizeInKb()
            _imageCacheSizeFlow.tryEmit(size)
        }
    }
}

/**
 * Get the size of the directory in kilobytes
 */
private fun File.getSizeInKb(): Long = walkTopDown().sumOf { it.length() } / 1024
