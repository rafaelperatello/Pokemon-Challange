package com.rafaelperatello.pokemonchallenge.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.widget.CardItem
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardBack
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardFront

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun PokemonDetails(
    modifier: Modifier = Modifier,
    pokemon: ShallowPokemon?,
    sharedTransitionScope: SharedTransitionScope,
    onBack: () -> Unit,
) {
    AnimatedContent(
        targetState = pokemon,
        transitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        label = "SnackEditDetails"
    ) { selectedPokemon ->
        if (selectedPokemon != null) {
            var isCardFlipped by remember {
                mutableStateOf(false)
            }

            val onNavigateBack = {
                if (isCardFlipped) {
                    isCardFlipped = false
                } else {
                    onBack()
                }
            }

            Box(
                modifier = modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNavigateBack
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                BackHandler(true, onBack = onNavigateBack)

                val rotation by animateFloatAsState(
                    targetValue = if (isCardFlipped) 180f else 0f,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = EaseInOut
                    ),
                    label = "pokemon_card_flip",
                )


                // Card Front
                CardItem(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            isCardFlipped = !isCardFlipped
                        }
                        .graphicsLayer {
                            alpha = if (rotation < 90) 1f else 0f
                            rotationY = rotation
                        }
                        .sharedBoundsPokemonCard(
                            selectedPokemon,
                            sharedTransitionScope,
                            this@AnimatedContent
                        ),
                    colors = CardColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp,
                        focusedElevation = 6.dp,
                    )
                ) {
                    PokemonCardFront(
                        modifier = Modifier
                            .graphicsLayer {
                            }
                            .fillMaxSize(),
                        imageModifier = Modifier.sharedElementPokemonImage(
                            selectedPokemon,
                            sharedTransitionScope,
                            this@AnimatedContent
                        ),
                        labelModifier = Modifier.sharedElementPokemonLabel(
                            selectedPokemon,
                            sharedTransitionScope,
                            this@AnimatedContent
                        ),
                        imageUrlLowRes = selectedPokemon.imageSmall,
//                        imageUrlHighRes = selectedPokemon.imageLarge,
                        filterQuality = FilterQuality.High,
                        showLabel = true,
                        pokemon = selectedPokemon,
                    )
                }

                // Card Back
                CardItem(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            isCardFlipped = !isCardFlipped
                        }
                        .graphicsLayer {
                            alpha = if (rotation > 90) 1f else 0f
                            rotationY = 180 + rotation
                        },
                    colors = CardColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp,
                        focusedElevation = 6.dp,
                    )
                ) {
                    PokemonCardBack(selectedPokemon)
                }
            }
        }
    }
}