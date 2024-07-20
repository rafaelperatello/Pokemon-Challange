package com.rafaelperatello.pokemonchallenge.ui.screen.home

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@Composable
internal fun HomeSettingsDialog(
    currentGridStyle: GridStyle = GridStyle.SMALL,
    onGridStyleUpdated: (GridStyle) -> Unit = { },
    onDismissRequest: () -> Unit = { }
) {
    var selectedStyle by remember {
        mutableStateOf(currentGridStyle)
    }

    val onDismissRequestInternal = {
        onDismissRequest()

        // Maybe update grid style after dismiss
        if (selectedStyle != currentGridStyle) {
            onGridStyleUpdated(selectedStyle)
        }
    }

    Dialog(onDismissRequest = onDismissRequestInternal) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 24.dp, 8.dp, 8.dp)
            ) {
                val radioOptions = GridStyle.entries.toTypedArray()

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = stringResource(id = R.string.grid_style)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    radioOptions.forEach { gridStyle ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (gridStyle == selectedStyle),
                                onClick = { selectedStyle = gridStyle }
                            )
                            Text(
                                text = stringResource(id = gridStyle.toStringRes()),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
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

private fun GridStyle.toStringRes(): Int {
    return when (this) {
        GridStyle.SMALL -> R.string.grid_style_small
        GridStyle.MEDIUM -> R.string.grid_style_medium
        GridStyle.LARGE -> R.string.grid_style_large
    }
}

@PreviewLightDark()
@Composable
fun HomeDialogPreview() {
    PokemonChallengeTheme() {
        HomeSettingsDialog()
    }
}