@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rafaelperatello.pokemonchallenge.MainRoutes
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import com.rafaelperatello.pokemonchallenge.ui.widget.AppBarAction
import com.rafaelperatello.pokemonchallenge.ui.widget.CardItem
import com.rafaelperatello.pokemonchallenge.ui.widget.ErrorWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.LoadingWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardBack
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonCardFront
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    currentRoute: State<MainRoutes?>,
    onUpdateAppBarAction: (AppBarAction?) -> Unit,
) {
    val viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute.value) {
        onUpdateAppBarAction(
            AppBarAction(
                imageVector = Icons.Outlined.Apps,
                onClick = { showDialog = true },
            ),
        )
    }

    if (showDialog) {
        HomeSettingsDialog(
            currentGridStyle = viewModel.gridStyle.value,
            onGridStyleUpdated = { style ->
                viewModel.onGridStyleUpdated(style)
            },
            onDismissRequest = { showDialog = false },
        )
    }

    val listState = viewModel.listState.collectAsLazyPagingItems()

    HomeContent(
        modifier = modifier,
        listState = listState,
        gridStyleFlow = viewModel.gridStyle,
        onRetryClick = { listState.retry() },
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    listState: LazyPagingItems<ShallowPokemon>,
    gridStyleFlow: StateFlow<GridStyle>,
    onRetryClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        val lazyGridState = rememberLazyGridState()

        when (listState.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingWidget(
                    modifier = Modifier.fillMaxSize(),
                    loadingSize = 64.dp,
                    strokeWidth = 8.dp,
                )
            }

            is LoadState.Error -> {
                ErrorWidget(
                    modifier = Modifier.fillMaxSize(),
                    textColor = MaterialTheme.colorScheme.onSurface,
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    errorDescription = stringResource(R.string.error_loading_data),
                    errorAction = stringResource(R.string.retry),
                    onRetryClick = onRetryClick,
                )
            }

            else -> {
                var selectedPokemon by remember {
                    mutableStateOf<ShallowPokemon?>(null)
                }

                var showDetails by remember {
                    mutableStateOf(false)
                }

                SharedTransitionLayout {
                    AnimatedContent(
                        targetState = showDetails,
                        label = "card_transition",
                    ) { targetState ->
                        if (!targetState) {
                            with(this@AnimatedContent) {
                                PokemonGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    lazyGridState = lazyGridState,
                                    listState = listState,
                                    gridStyleFlow = gridStyleFlow,
                                    onPokemonClick = {
                                        selectedPokemon = it
                                        showDetails = true
                                    },
                                    onRetryClick = onRetryClick,
                                    animatedVisibilityScope = this,
                                    sharedTransitionScope = this@SharedTransitionLayout
                                )
                            }
                        } else {
                            PokemonDetails(
                                modifier = Modifier.fillMaxSize(),
                                pokemon = selectedPokemon!!,
                                animatedVisibilityScope = this,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                onBack = {
                                    showDetails = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun PokemonGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    listState: LazyPagingItems<ShallowPokemon>,
    gridStyleFlow: StateFlow<GridStyle>,
    onPokemonClick: (ShallowPokemon) -> Unit = {},
    onRetryClick: () -> Unit = {},
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val gridStyle by gridStyleFlow.collectAsState()
    val columnsCount by remember(gridStyle) {
        mutableIntStateOf(
            when (gridStyle) {
                GridStyle.SMALL -> 4
                GridStyle.MEDIUM -> 3
                GridStyle.LARGE -> 2
            },
        )
    }

    val filterQuality by remember(gridStyle) {
        mutableStateOf(
            when (gridStyle) {
                GridStyle.SMALL -> FilterQuality.None
                GridStyle.MEDIUM -> FilterQuality.Low
                GridStyle.LARGE -> FilterQuality.High
            },
        )
    }

    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(16.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        items(listState.itemCount, key = { it }) {
            with(sharedTransitionScope) {
                val pokemon = listState[it] ?: return@with

                CardItem(
                    modifier = Modifier.sharedBoundsPokemonCard(
                        pokemon,
                        sharedTransitionScope,
                        animatedVisibilityScope
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    PokemonCardFront(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onPokemonClick(pokemon) },
                        imageModifier = Modifier.sharedElementPokemonImage(
                            pokemon,
                            sharedTransitionScope,
                            animatedVisibilityScope
                        ),
                        labelModifier = Modifier.sharedElementPokemonLabel(
                            pokemon,
                            sharedTransitionScope,
                            animatedVisibilityScope
                        ),
                        imageUrlLowRes = pokemon.imageSmall ?: "",
                        filterQuality = filterQuality,
                        showLabel = true,
                        pokemon = pokemon,
                    )
                }
            }
        }

        when (listState.loadState.append) {
            is LoadState.Error -> {
                item {
                    CardItem(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                    ) {
                        ErrorWidget(
                            modifier = Modifier.fillMaxSize(),
                            textColor = MaterialTheme.colorScheme.onErrorContainer,
                            buttonColor = MaterialTheme.colorScheme.error,
                            onRetryClick = onRetryClick,
                            errorDescription = stringResource(R.string.error_loading_page),
                            errorAction = stringResource(R.string.retry),
                        )
                    }
                }
            }

            is LoadState.Loading -> {
                item {
                    CardItem(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                    ) {
                        LoadingWidget(
                            modifier = Modifier.fillMaxSize(),
                            loadingSize = 48.dp,
                            strokeWidth = 8.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }

            else -> Unit
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun PokemonDetails(
    modifier: Modifier = Modifier,
    pokemon: ShallowPokemon,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(24.dp),
    ) {

        var isCardFlipped by remember {
            mutableStateOf(false)
        }

        val rotation by animateFloatAsState(
            targetValue = if (isCardFlipped) 180f else 0f,
            animationSpec = tween(
                durationMillis = 700,
                easing = EaseInOut
            ),
            label = "pokemon_card_flip",
        )

        BackHandler(true) {
            if (isCardFlipped) {
                isCardFlipped = false
            } else {
                onBack()
            }
        }

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
                    pokemon,
                    sharedTransitionScope,
                    animatedVisibilityScope
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
                    pokemon,
                    sharedTransitionScope,
                    animatedVisibilityScope
                ),
                labelModifier = Modifier.sharedElementPokemonLabel(
                    pokemon,
                    sharedTransitionScope,
                    animatedVisibilityScope
                ),
                imageUrlLowRes = pokemon.imageSmall ?: "",
//                imageUrlHighRes = pokemon.imageLarge,
                filterQuality = FilterQuality.High,
                showLabel = true,
                pokemon = pokemon,
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
            PokemonCardBack(pokemon)
        }
    }
}

@Composable
private fun Modifier.sharedElementPokemonLabel(
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
private fun Modifier.sharedElementPokemonImage(
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

@Composable
private fun Modifier.sharedBoundsPokemonCard(
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