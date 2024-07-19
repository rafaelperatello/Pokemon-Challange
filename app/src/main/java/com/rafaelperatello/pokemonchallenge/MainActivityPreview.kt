package com.rafaelperatello.pokemonchallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@PreviewLightDark()
@Composable
fun ContentPreview() {
    PokemonChallengeTheme() {
        PokemonScaffold()
    }
}
