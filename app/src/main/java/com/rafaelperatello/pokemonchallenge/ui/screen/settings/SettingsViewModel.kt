package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.util.SettingsUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class SettingsViewModel(
    private val settingsUtil: SettingsUtil,
    private val pokemonDb: PokemonDatabase,
    private val ioDispatcher: CoroutineContext,
) : ViewModel() {

    private val _settingState: MutableStateFlow<SettingsState> =
        MutableStateFlow(
            SettingsState(
                httpCacheSize = 0,
                imageCacheSize = 0,
            )
        )

    val settingState: StateFlow<SettingsState> get() = _settingState

    private val _viewEvent: MutableStateFlow<SettingsViewEvent> =
        MutableStateFlow(SettingsViewEvent.None)
    val viewEvent: StateFlow<SettingsViewEvent>
        get() = _viewEvent.asStateFlow()

    init {
        listenToSettings()
    }

    private fun listenToSettings() {
        Log.d("SettingsViewModel", "listenToSettings: start")

        viewModelScope.launch {
            with(settingsUtil) {
                httpCacheSizeFlow.combine(imageCacheSizeFlow) { httpCacheSize, imageCacheSize ->
                    SettingsState(
                        httpCacheSize = httpCacheSize,
                        imageCacheSize = imageCacheSize,
                    )
                }.collectLatest {
                    Log.d("SettingsViewModel", "listenToSettings: $it")
                    _settingState.value = it
                }
            }
        }
    }

    fun onClearDatabase() {
        viewModelScope.launch(context = ioDispatcher) {
            pokemonDb.clearAllTables()
            _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.DATABASE_RESET)
        }

    }

    fun onClearHttpCache() {
        viewModelScope.launch {
            val success = settingsUtil.clearHttpCache()
            if (success) {
                _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.HTTP_CACHE_CLEARED)
            }// Todo show error?
        }

    }

    fun onClearImageCache() {
        viewModelScope.launch {
            val success = settingsUtil.clearImageCache()
            if (success) {
                _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.IMAGE_CACHE_CLEARED)
            } // Todo show error?
        }
    }

    fun onRefresh() {
        Log.d("SettingsViewModel", "onRefresh")
    }
}

internal data class SettingsState(
    val httpCacheSize: Long = 0,
    val imageCacheSize: Long = 0,
)

@Stable
internal sealed class SettingsViewEvent(var consumed: Boolean = false) {
    data object None : SettingsViewEvent()
    data class ShowSnackbar(val snackbarType: SnackbarType) : SettingsViewEvent()
}

internal enum class SnackbarType {
    DATABASE_RESET,
    HTTP_CACHE_CLEARED,
    IMAGE_CACHE_CLEARED,
}
