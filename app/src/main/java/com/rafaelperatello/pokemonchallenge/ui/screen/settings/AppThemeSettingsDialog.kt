package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.settings.AppTheme
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@Composable
internal fun AppThemeSettingsDialog(
    currentAppTheme: AppTheme = AppTheme.SYSTEM,
    onThemeUpdated: (AppTheme) -> Unit = { },
    onDismissRequest: () -> Unit = { },
) {
    var selectedStyle by remember {
        mutableStateOf(currentAppTheme)
    }

    val onDismissRequestInternal = {
        onDismissRequest()

        // Only update theme after dismiss
        if (selectedStyle != currentAppTheme) {
            onThemeUpdated(selectedStyle)
        }
    }

    Dialog(onDismissRequest = onDismissRequestInternal) {
        Card(
            modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 24.dp, 8.dp, 8.dp),
            ) {
                val radioOptions = AppTheme.entries.toTypedArray()

                Text(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = stringResource(id = R.string.grid_style),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    radioOptions.forEach { appTheme ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (appTheme == selectedStyle),
                                onClick = { selectedStyle = appTheme },
                            )
                            Text(
                                text = stringResource(id = appTheme.toStringRes()),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        selectedStyle = appTheme
                                    }
                                    .padding(8.dp),
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequestInternal() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.close))
                    }
                }
            }
        }
    }
}

private fun AppTheme.toStringRes(): Int =
    when (this) {
        AppTheme.LIGHT -> R.string.app_theme_light
        AppTheme.DARK -> R.string.app_theme_dark
        AppTheme.SYSTEM -> R.string.app_theme_system
    }

@PreviewLightDark()
@Composable
fun HomeDialogPreview() {
    PokemonChallengeTheme {
        AppThemeSettingsDialog()
    }
}
