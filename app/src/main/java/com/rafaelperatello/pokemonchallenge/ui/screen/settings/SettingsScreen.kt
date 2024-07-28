package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.settings.AppTheme
import com.rafaelperatello.pokemonchallenge.ui.widget.LoadingWidget
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(snackbarHostState: SnackbarHostState) {
    val viewModel: SettingsViewModel = koinViewModel<SettingsViewModel>()

    val settingsState by viewModel.settingState.collectAsStateWithLifecycle()
    val settingsViewEvent by viewModel.viewEvent.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(settingsState) {
        Log.d("SettingsScreen", "settingsState: $settingsState")
    }

    SettingsSnackBar(settingsViewEvent, snackbarHostState, context)

    when (settingsState) {
        is SettingViewState.Loading -> {
            LoadingWidget(modifier = Modifier.fillMaxSize())
        }

        is SettingViewState.Ready -> {
            val readyState = settingsState as SettingViewState.Ready
            SettingsPanel(readyState.settingsState, viewModel, context)
        }
    }
}

@Composable
internal fun SettingsPanel(
    settingsState: SettingsState,
    viewModel: SettingsViewModel,
    context: Context,
) {
    var showAppThemeDialog by remember { mutableStateOf(false) }
    if (showAppThemeDialog) {
        AppThemeSettingsDialog(
            currentAppTheme = settingsState.appTheme,
            onThemeUpdated = { viewModel.onChangeAppTheme(it) },
            onDismissRequest = { showAppThemeDialog = false },
        )
    }

    val appThemeState =
        remember(settingsState) {
            val themeRes =
                when (settingsState.appTheme) {
                    AppTheme.LIGHT -> R.string.app_theme_light
                    AppTheme.DARK -> R.string.app_theme_dark
                    AppTheme.SYSTEM -> R.string.app_theme_system
                }

            mutableStateOf(
                context.getString(
                    R.string.description_current_theme,
                    context.getString(themeRes)
                )
            )
        }

    val descriptionNetworkState =
        remember(settingsState) {
            mutableStateOf(
                context.getString(
                    R.string.description_cache_size_kb,
                    settingsState.networkCacheSize,
                )
            )
        }

    val descriptionImageState =
        remember(settingsState) {
            mutableStateOf(
                context.getString(
                    R.string.description_cache_size_kb,
                    settingsState.imageCacheSize,
                )
            )
        }

    val settingItemDataList =
        listOf(
            SettingItemData(
                label = stringResource(id = R.string.setting_app_theme),
                description = appThemeState,
                buttonText = stringResource(id = R.string.change),
                onClick = { showAppThemeDialog = true },
            ),
            SettingItemData(
                label = stringResource(id = R.string.setting_database),
                description = remember { mutableStateOf(context.getString(R.string.description_reset_pokemon_table)) },
                buttonText = stringResource(id = R.string.reset),
                onClick = { viewModel.onClearDatabase() },
            ),
            SettingItemData(
                label = stringResource(id = R.string.setting_network),
                description = descriptionNetworkState,
                buttonText = stringResource(id = R.string.clear),
                onClick = { viewModel.onClearNetworkCache() },
            ),
            SettingItemData(
                label = stringResource(id = R.string.setting_image),
                description = descriptionImageState,
                buttonText = stringResource(id = R.string.clear),
                onClick = { viewModel.onClearImageCache() },
            ),
        )

    SettingsContent(settingItemDataList)
}

@Composable
private fun SettingsSnackBar(
    settingsViewEvent: SettingsViewEvent,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    LaunchedEffect(key1 = settingsViewEvent) {
        Log.d("SettingsScreen", "settingsViewEvent: $settingsViewEvent")

        val event = settingsViewEvent
        if (event.consumed) return@LaunchedEffect
        event.consumed = true

        when (event) {
            SettingsViewEvent.None -> Unit

            is SettingsViewEvent.ShowSnackbar -> {
                val message =
                    when (event.snackbarType) {
                        SnackbarType.APP_THEME_CHANGED -> R.string.snackbar_app_theme_changed
                        SnackbarType.IMAGE_CACHE_CLEARED -> R.string.snackbar_image_cache_cleared
                        SnackbarType.DATABASE_RESET -> R.string.snackbar_database_reset
                        SnackbarType.NETWORK_CACHE_CLEARED -> R.string.snackbar_network_cache_cleared
                    }

                snackbarHostState.showSnackbar(
                    message = context.getString(message),
                    duration = SnackbarDuration.Short,
                )
            }
        }

        event.consumed = true
    }
}

@Composable
internal fun SettingsContent(
    settingsState: List<SettingItemData>,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp, 24.dp),
        ) {
            settingsState.forEach {
                SettingItem(
                    label = it.label,
                    description = it.description.value,
                    buttonText = it.buttonText,
                    onClick = it.onClick,
                )

                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
internal fun SettingItem(
    label: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit = {},
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = description,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.size(8.dp))

                FilledTonalButton(
                    modifier = Modifier.wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
                    onClick = onClick,
                ) {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
        }
    }
}

internal data class SettingItemData(
    val label: String,
    val description: State<String>,
    val buttonText: String,
    val onClick: () -> Unit = {},
)
