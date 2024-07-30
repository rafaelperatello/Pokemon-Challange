package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.widget.CardItem
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardBack
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardFront
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.abs


private sealed class CardAnimationAction {
    data class Drag(val value: Float) : CardAnimationAction()
    data object FinishFlip : CardAnimationAction()
    data object FlipFront : CardAnimationAction()
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
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
            val rotation = remember {
                Animatable(0f)
            }

            var isDragging by remember {
                mutableStateOf(false)
            }

            val isCardFlipped by remember(rotation.value) {
                val isFlipped = abs(rotation.value) % 360 in 90f..270f
                // Log.d("Card", "isCardFlipped: $isFlipped")
                mutableStateOf(isFlipped)
            }

            val showCardFront by remember(rotation.value) {
                derivedStateOf {
                    !isCardFlipped
                }
            }
            LaunchedEffect(showCardFront) {
                Log.d("Card", "showCardFront: $showCardFront")
            }

            val dragFlow = remember {
                MutableSharedFlow<CardAnimationAction>(
                    replay = 1,
                    extraBufferCapacity = 5,
                    onBufferOverflow = BufferOverflow.DROP_OLDEST
                )
            }

            val density = LocalDensity.current
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp.value

            LaunchedEffect(isDragging) {
                dragFlow.collect {
                    when (it) {
                        is CardAnimationAction.Drag -> {
                            with(density) {
                                val dpRotation = it.value.toDp().value
                                val newRotation = (rotation.value + dpRotation) % 360
                                rotation.snapTo(newRotation)
                                Log.d(
                                    "Card",
                                    "Drag: ${rotation.value} | screenWidth: $screenWidth"
                                )
                            }
                        }

                        is CardAnimationAction.FinishFlip -> {
                            val target =
                                if (rotation.value > 0) {
                                    when (rotation.value) {
                                        in 0f..90f -> 0f
                                        in 90f..180f -> 180f
                                        in 180f..270f -> 180f
                                        in 270f..360f -> 360f
                                        else -> 0f
                                    }
                                } else {
                                    when (rotation.value) {
                                        in -90f..0f -> 0f
                                        in -180f..-90f -> -180f
                                        in -270f..-180f -> -180f
                                        in -360f..-270f -> -360f
                                        else -> 0f
                                    }
                                }

                            rotation.animateTo(
                                targetValue = target,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }

                        is CardAnimationAction.FlipFront -> {
                            rotation.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    }
                }
            }

            val coroutineScope = rememberCoroutineScope()
            val onNavigateBack = {
                if (isCardFlipped) {
                    coroutineScope.launch {
                        dragFlow.tryEmit(CardAnimationAction.FlipFront)
                    }
                    Unit
                } else {
                    onBack()
                }
            }

            Box(
                modifier = modifier
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState(
                            onDelta = { delta ->
                                dragFlow.tryEmit(CardAnimationAction.Drag(delta))
                            }
                        ),
                        onDragStarted = {
                            isDragging = true
                            Log.d("Card", "Drag started: $it")
                        },
                        onDragStopped = {
                            isDragging = false
                            dragFlow.tryEmit(CardAnimationAction.FinishFlip)
                            Log.d("Card", "Drag stopped: $it")
                        }
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                BackHandler(true, onBack = onNavigateBack)

                // Card Front
                if (showCardFront) {
                    CardItem(
                        modifier = Modifier
                            .padding(16.dp)
                            .graphicsLayer {
                                val absRotation = abs(rotation.value)
                                val rotationIn360 = absRotation % 360
                                alpha =
                                    if (rotationIn360 in 0f..90f ||
                                        rotationIn360 in 270f..360f
                                    ) 1f
                                    else 0f

                                rotationY = rotation.value
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
                }

                // Card Back
                if (!showCardFront) {
                    CardItem(
                        modifier = Modifier
                            .padding(16.dp)
                            .graphicsLayer {
                                val absRotation = abs(rotation.value)
                                val rotationIn360 = absRotation % 360
                                alpha = if ((rotationIn360) in 90f..270f) 1f else 0f
                                rotationY = 180 + rotation.value
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
}
