package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.rafaelperatello.pokemonchallenge.MainRoutes
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import com.rafaelperatello.pokemonchallenge.ui.widget.AppBarAction
import com.rafaelperatello.pokemonchallenge.ui.widget.CardItem
import com.rafaelperatello.pokemonchallenge.ui.widget.ErrorWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.LoadingWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonImage
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

                SharedTransitionLayout() {
                    AnimatedContent(
                        targetState = showDetails,
                        label = "card_transition",

                        ) { targetState ->
                        if (!targetState) {
                            with(this@AnimatedContent) {
                                PokemonGrid(
                                    modifier = Modifier
                                        .fillMaxSize(),
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
                            DetailsContent(
                                modifier = Modifier.fillMaxSize(),
                                pokemon = selectedPokemon!!,
                                animatedVisibilityScope = this,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                onBack = {
                                    showDetails = false
//                                    selectedPokemon = null
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
private fun DetailsContent(
    modifier: Modifier = Modifier,
    pokemon: ShallowPokemon,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .clip(MaterialTheme.shapes.medium),
    ) {
        BackHandler(true) {
            onBack()
        }

        with(sharedTransitionScope) {
            PokemonImage(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState("pokemon-${pokemon.imageSmall}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { size, layoutCoordinates ->
                            spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium,
                            )
                        },
                    ),
                imageUrl = pokemon.imageSmall ?: "",
//                imageUrl = pokemon.imageLarge ?: pokemon.imageSmall ?: "", // Todo handle
                filterQuality = FilterQuality.High,
                showLabel = false,
                pokemon = pokemon,
                onPokemonClick = { onBack() },
            )
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
            CardItem(
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                val pokemon = listState[it] ?: return@CardItem

                with(sharedTransitionScope) {
                    PokemonImage(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState("pokemon-${pokemon.imageSmall}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { size, layoutCoordinates ->
                                spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow,
                                )
                            }
                        ),
                        imageUrl = pokemon.imageSmall ?: "",
                        filterQuality = filterQuality,
                        showLabel = true,
                        pokemon = pokemon,
                        onPokemonClick = onPokemonClick,
                    )
                }
            }
        }

        when (listState.loadState.append) {
            is LoadState.Error -> {
                item {
                    CardItem(
                        color = MaterialTheme.colorScheme.errorContainer,
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
                        color = MaterialTheme.colorScheme.background,
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
