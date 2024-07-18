package com.rafaelperatello.pokemonchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import com.rafaelperatello.pokemonchallenge.ui.widget.ErrorWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.LoadingWidget
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonAppBar
import com.rafaelperatello.pokemonchallenge.ui.widget.PokemonImage
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonChallengeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        PokemonAppBar(stringResource(id = R.string.title_main))
                    }
                ) { innerPadding ->
                    Content(
                        modifier = Modifier.padding(innerPadding),
                        viewState = viewModel.state,
                        listState = viewModel.listState,
                        onRetryClick = { viewModel.onRetryClick() },
                        onFetchNextPage = { viewModel.fetchNextPage() },
                        onPokemonClick = { pokemon ->
                            // Todo navigate to details
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun Content(
    modifier: Modifier = Modifier,
    viewState: StateFlow<MainViewModelState>,
    listState: StateFlow<ListState>,
    onRetryClick: () -> Unit = {},
    onFetchNextPage: () -> Unit = {},
    onPokemonClick: (ShallowPokemon) -> Unit = {}
) {
    Surface(
        modifier = modifier,
    ) {
        val state = viewState.collectAsState()

        when (val value = state.value) {
            is MainViewModelState.Loading -> {
                LoadingWidget(modifier = Modifier.fillMaxSize())
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
                    pokemonList = value.pokemonList,
                    onPokemonClick = onPokemonClick,
                    onRetryClick = onFetchNextPage
                )
            }

            is MainViewModelState.Error -> {
                ErrorWidget(
                    modifier = Modifier.fillMaxSize(),
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
    pokemonList: List<ShallowPokemon>,
    onPokemonClick: (ShallowPokemon) -> Unit = {},
    onRetryClick: () -> Unit = {}
) {
    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(3.dp),
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

        // item(key = listState) {
        item() {
            val newListState by listState.collectAsState()
            Column(
                modifier = Modifier
                    .aspectRatio(0.72f)
                    .padding(3.dp)
            ) {
                when (newListState) {
                    ListState.PAGINATING -> {
                        LoadingWidget(
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    ListState.ERROR -> {
                        ErrorWidget(
                            modifier = Modifier.fillMaxSize(),
                            onRetryClick = onRetryClick,
                            errorDescription = stringResource(R.string.error_loading_page),
                            errorAction = stringResource(R.string.retry),
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}