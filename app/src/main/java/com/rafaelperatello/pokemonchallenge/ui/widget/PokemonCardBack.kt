package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@Composable
internal fun PokemonCardBack(pokemon: ShallowPokemon) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .alpha(0.5f),
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.surfaceContainer,
                blendMode = BlendMode.SrcIn
            ),
            imageVector = ImageVector.vectorResource(id = R.drawable.pokeball_shape_no_stroke),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = pokemon.name,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PokemonCardBackPreview() {
    PokemonChallengeTheme() {
        PokemonCardBack(
            pokemon = ShallowPokemon(
                id = 1,
                name = "Bulbasaur",
            )
        )
    }
}