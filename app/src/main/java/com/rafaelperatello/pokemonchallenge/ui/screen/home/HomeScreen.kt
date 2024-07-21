package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.MainRoutes
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import com.rafaelperatello.pokemonchallenge.ui.widget.AppBarAction
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
    val viewModel: MainViewModel = koinViewModel<MainViewModel>()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute.value) {
        onUpdateAppBarAction(
            AppBarAction(
                imageVector = Icons.Outlined.Apps,
                onClick = { showDialog = true }
            )
        )
    }

    if (showDialog) {
        HomeSettingsDialog(
            currentGridStyle = viewModel.gridStyle.value,
            onGridStyleUpdated = { style ->
                viewModel.onGridStyleUpdated(style)
            },
            onDismissRequest = { showDialog = false }
        )
    }

    HomeContent(
        modifier = modifier,
        viewState = viewModel.state,
        listState = viewModel.listState,
        gridStyleFlow = viewModel.gridStyle,
        onRetryClick = { viewModel.onRetryClick() },
        onFetchNextPage = { viewModel.fetchNextPage() },
        onPokemonClick = { pokemon ->
            // Todo navigate to details
        }
    )
}

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    viewState: StateFlow<MainViewModelState>,
    listState: StateFlow<ListState>,
    gridStyleFlow: StateFlow<GridStyle>,
    onRetryClick: () -> Unit = {},
    onFetchNextPage: () -> Unit = {},
    onPokemonClick: (ShallowPokemon) -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        val state = viewState.collectAsState()

        when (val value = state.value) {
            is MainViewModelState.Loading -> {
                LoadingWidget(
                    modifier = Modifier.fillMaxSize(),
                    loadingSize = 64.dp,
                    strokeWidth = 8.dp
                )
            }

            is MainViewModelState.Success -> {
                val lazyGridState = rememberLazyGridState()

                val shouldStartPaginate = remember {
                    derivedStateOf {
                        val lastVisibleItemIndex =
                            lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount

                        (lastVisibleItemIndex ?: -10) >= totalItemsCount - 10
                    }
                }

                LaunchedEffect(key1 = shouldStartPaginate.value) {
                    if (shouldStartPaginate.value && listState.value == ListState.IDLE) {
                        onFetchNextPage()
                    }
                }

                PokemonGrid(
                    modifier = Modifier.fillMaxSize(),
                    lazyGridState = lazyGridState,
                    listState = listState,
                    gridStyleFlow = gridStyleFlow,
                    pokemonList = value.pokemonList,
                    onPokemonClick = onPokemonClick,
                    onRetryClick = onFetchNextPage
                )
            }

            is MainViewModelState.Error -> {
                ErrorWidget(
                    modifier = Modifier.fillMaxSize(),
                    textColor = MaterialTheme.colorScheme.onSurface,
                    buttonColor = MaterialTheme.colorScheme.tertiary,
                    errorDescription = stringResource(R.string.error_loading_data),
                    errorAction = stringResource(R.string.retry),
                    onRetryClick = onRetryClick
                )
            }
        }
    }
}

// Todo validate stability api
@Composable
internal fun PokemonGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    listState: StateFlow<ListState>,
    gridStyleFlow: StateFlow<GridStyle>,
    pokemonList: List<ShallowPokemon>,
    onPokemonClick: (ShallowPokemon) -> Unit = {},
    onRetryClick: () -> Unit = {}
) {

    val gridStyle by gridStyleFlow.collectAsState()
    val columnsCount by remember(gridStyle) {
        mutableIntStateOf(
            when (gridStyle) {
                GridStyle.SMALL -> 4
                GridStyle.MEDIUM -> 3
                GridStyle.LARGE -> 2
            }
        )
    }

    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(16.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(pokemonList.size, key = { it }) {
            PokemonImage(
                modifier = Modifier
                    .aspectRatio(0.72f)
                    .padding(3.dp),
                pokemon = pokemonList[it],
                position = it,
                onPokemonClick = onPokemonClick
            )
        }

        item() {
            val newListState by listState.collectAsState()
            val color = when (listState.value) {
                ListState.ERROR -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }

            Surface(
                modifier = Modifier
                    .aspectRatio(0.72f)
                    .padding(3.dp),
                color = color,
                shape = MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier
                        .aspectRatio(0.72f)
                        .padding(3.dp)
                ) {
                    when (newListState) {
                        ListState.PAGINATING -> {
                            LoadingWidget(
                                modifier = Modifier.fillMaxSize(),
                                loadingSize = 48.dp,
                                strokeWidth = 8.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        ListState.ERROR -> {
                            ErrorWidget(
                                modifier = Modifier.fillMaxSize(),
                                textColor = MaterialTheme.colorScheme.onErrorContainer,
                                buttonColor = MaterialTheme.colorScheme.error,
                                onRetryClick = onRetryClick,
                                errorDescription = stringResource(R.string.error_loading_page),
                                errorAction = stringResource(R.string.retry)
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}