package com.rafaelperatello.pokemonchallenge.util

import android.os.FileObserver
import android.util.Log
import com.rafaelperatello.pokemonchallenge.util.extensions.throttleLatest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val FILE_OBSERVER_MASK: Int = FileObserver.CREATE or
        FileObserver.DELETE or
        FileObserver.DELETE_SELF or
        FileObserver.MODIFY or
        FileObserver.MOVE_SELF or
        FileObserver.MOVED_FROM or
        FileObserver.MOVED_TO

internal class CacheFileObserver(
    private val dir: File,
    ioContext: CoroutineContext,
) :
    FileObserver(dir, FILE_OBSERVER_MASK),
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + ioContext

    val id: String = dir.name

    private val _dataChangedFlow: MutableSharedFlow<Unit> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    val dataChangedFlow: Flow<Unit>
        get() = _dataChangedFlow.throttleLatest(1.toDuration(DurationUnit.SECONDS))

    init {
        launch {
            _dataChangedFlow.subscriptionCount.collect {
                Log.d("CacheFileObserver", "${dir.name}: subscriptionCount: $it")

                if (it > 0) {
                    _dataChangedFlow.tryEmit(Unit)
                    Log.d("CacheFileObserver", "${dir.name} startWatching")
                    startWatching()
                } else {
                    Log.d("CacheFileObserver", "${dir.name} stopWatching")
                    stopWatching()
                }
            }
        }
    }

    override fun onEvent(
        event: Int,
        path: String?,
    ) {
        // Log.d("CacheFileObserver", "${dir.name} event:${event.toEventString()} $path")
        _dataChangedFlow.tryEmit(Unit)
    }
}

private fun Int.toEventString(): String =
    when (this) {
        FileObserver.ACCESS -> "ACCESS"
        FileObserver.ATTRIB -> "ATTRIB"
        FileObserver.CLOSE_NOWRITE -> "CLOSE_NOWRITE"
        FileObserver.CLOSE_WRITE -> "CLOSE_WRITE"
        FileObserver.CREATE -> "CREATE"
        FileObserver.DELETE -> "DELETE"
        FileObserver.DELETE_SELF -> "DELETE_SELF"
        FileObserver.MODIFY -> "MODIFY"
        FileObserver.MOVED_FROM -> "MOVED_FROM"
        FileObserver.MOVED_TO -> "MOVED_TO"
        FileObserver.OPEN -> "OPEN"
        else -> "UNKNOWN"
    }
