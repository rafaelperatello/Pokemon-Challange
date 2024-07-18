package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokemonAppBar(
    title: String
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@PreviewLightDark()
@Composable
fun AppBarPreview() {
    PokemonChallengeTheme() {
        PokemonAppBar("Pokemon Challenge")
    }
}