@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.rafaelperatello.pokemonchallenge.ui.screen.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon

@Composable
internal fun Modifier.sharedBoundsPokemonCard(
    pokemon: ShallowPokemon,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) = with(sharedTransitionScope) {
    sharedBounds(
        sharedContentState = rememberSharedContentState("pokemon-${pokemon.id}"),
        animatedVisibilityScope = animatedVisibilityScope,
        boundsTransform = { size, layoutCoordinates ->
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            )
        },
        renderInOverlayDuringTransition = false
    )
}

@Composable
internal fun Modifier.sharedElementPokemonLabel(
    pokemon: ShallowPokemon,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) = with(sharedTransitionScope) {
    sharedElement(
        state = rememberSharedContentState("pokemon-label-${pokemon.id}"),
        animatedVisibilityScope = animatedVisibilityScope,
        boundsTransform = { size, layoutCoordinates ->
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            )
        },
    )
}

@Composable
internal fun Modifier.sharedElementPokemonImage(
    pokemon: ShallowPokemon,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) = with(sharedTransitionScope) {
    sharedElement(
        state = rememberSharedContentState("pokemon-${pokemon.imageSmall}"),
        animatedVisibilityScope = animatedVisibilityScope,
        boundsTransform = { size, layoutCoordinates ->
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            )
        },
        clipInOverlayDuringTransition = OverlayClip(
            clipShape = MaterialTheme.shapes.small
        ),
    )
}
