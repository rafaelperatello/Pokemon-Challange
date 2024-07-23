package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.ImageConstants
import com.rafaelperatello.pokemonchallenge.NetworkConstants
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabase
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun SettingsScreen(
    snackbarHostState: SnackbarHostState
) {
    val database = koinInject<PokemonDatabase>()

    SettingsContent(
        database = database,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun SettingsContent(
    database: PokemonDatabase? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 24.dp)
        ) {
            SettingItem(
                label = "Database", // Todo String resource
                buttonText = "Reset", // Todo String resource
                onClick = {
                    coroutineScope.launch {
                        database?.pokemonDao()?.deleteAll()

                        snackbarHostState.showSnackbar(
                            message = "Database reset", // Todo String resource
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            SettingItem(
                label = "Http Cache", // Todo String resource
                buttonText = "Clear", // Todo String resource
                onClick = {
                    coroutineScope.launch {
                        val dir = context.cacheDir.resolve(NetworkConstants.Cache.DIRECTORY)
                        dir.deleteRecursively()

                        snackbarHostState.showSnackbar(
                            message = "Http cache cleared", // Todo String resource
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            SettingItem(
                label = "Image Cache", // Todo String resource
                buttonText = "Clear", // Todo String resource
                onClick = {
                    coroutineScope.launch {
                        val dir = context.cacheDir.resolve(ImageConstants.Cache.DIRECTORY)
                        dir.deleteRecursively()

                        snackbarHostState.showSnackbar(
                            message = "Image cache cleared", // Todo String resource
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        }
    }
}

@Composable
internal fun SettingItem(
    label: String,
    buttonText: String,
    onClick: () -> Unit = {},
) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = TextUnit(20f, TextUnitType.Sp)
    )

    Spacer(modifier = Modifier.size(8.dp))

    ElevatedButton(
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick
    ) {
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}
