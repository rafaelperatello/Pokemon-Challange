package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import com.rafaelperatello.pokemonchallenge.domain.settings.AppTheme
import com.rafaelperatello.pokemonchallenge.util.CacheUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class SettingsViewModel(
    private val ioDispatcher: CoroutineContext,
    private val cacheUtil: CacheUtil,
    private val pokemonDb: PokemonDatabase,
    private val appSettings: AppSettings,
) : ViewModel() {

    val settingState: StateFlow<SettingViewState> =
        combine(
            appSettings.appTheme,
            cacheUtil.networkCacheSizeFlow,
            cacheUtil.imageCacheSizeFlow
        ) { appTheme, networkCacheSize, imageCacheSize ->
            Log.d(
                "SettingsViewModel",
                "upstream - state update received: $networkCacheSize, $imageCacheSize"
            )

            SettingViewState.Ready(
                SettingsState(
                    appTheme = appTheme,
                    networkCacheSize = networkCacheSize,
                    imageCacheSize = imageCacheSize,
                )
            )
        }.distinctUntilChanged()
            .onEach { Log.d("SettingsViewModel", "upstream - distinct state update: $it") }
            .onStart { Log.d("SettingsViewModel", "upstream - state update started") }
            .onCompletion { Log.d("SettingsViewModel", "upstream - state update completed") }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5.toDuration(DurationUnit.SECONDS)),
                SettingViewState.Loading
            )

    private val _viewEvent: MutableStateFlow<SettingsViewEvent> =
        MutableStateFlow(SettingsViewEvent.None)
    val viewEvent: StateFlow<SettingsViewEvent>
        get() = _viewEvent.asStateFlow()

    fun onChangeAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            appSettings.setAppTheme(theme)
            _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.APP_THEME_CHANGED)
        }
    }

    fun onClearDatabase() {
        viewModelScope.launch(context = ioDispatcher) {
            pokemonDb.clearAllTables()
            _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.DATABASE_RESET)
        }
    }

    fun onClearNetworkCache() {
        viewModelScope.launch {
            val success = cacheUtil.clearNetworkCache()
            if (success) {
                _viewEvent.value =
                    SettingsViewEvent.ShowSnackbar(SnackbarType.NETWORK_CACHE_CLEARED)
            }
        }
    }

    fun onClearImageCache() {
        viewModelScope.launch {
            val success = cacheUtil.clearImageCache()
            if (success) {
                _viewEvent.value = SettingsViewEvent.ShowSnackbar(SnackbarType.IMAGE_CACHE_CLEARED)
            }
        }
    }
}

internal sealed class SettingViewState {
    data object Loading : SettingViewState()
    data class Ready(val settingsState: SettingsState) : SettingViewState()
}

internal data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val networkCacheSize: Long = 0,
    val imageCacheSize: Long = 0,
)

@Stable
internal sealed class SettingsViewEvent(
    var consumed: Boolean = false,
) {
    data object None : SettingsViewEvent()

    data class ShowSnackbar(val snackbarType: SnackbarType) : SettingsViewEvent()
}

internal enum class SnackbarType {
    APP_THEME_CHANGED,
    DATABASE_RESET,
    NETWORK_CACHE_CLEARED,
    IMAGE_CACHE_CLEARED,
}
