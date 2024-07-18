package com.rafaelperatello.pokemonchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.PokemonImages
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
                    topBar = { AppBar() }
                ) { innerPadding ->
                    Content(
                        modifier = Modifier.padding(innerPadding),
                        viewState = viewModel.state
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.title_main)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    viewState: StateFlow<MainViewModelState>,
) {
    Surface(
        modifier = modifier,
    ) {
        val state = viewState.collectAsState()

        when (val value = state.value) {
            is MainViewModelState.Loading -> {
                Loading()
            }

            is MainViewModelState.Success -> {
                PokemonGrid(
                    modifier = Modifier.fillMaxSize(),
                    pokemonList = value.pokemonList
                )
            }

            is MainViewModelState.Error -> {
                Error()
            }
        }
    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun Error() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Todo string resource
        Text(text = "Error")
    }
}

// Todo validate stability
@Composable
private fun PokemonGrid(
    modifier: Modifier = Modifier,
    pokemonList: List<ShallowPokemon>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(pokemonList.size, key = { it }) {
            PokemonImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(3.dp),
                pokemon = pokemonList[it]
            )
        }
    }
}

@Composable
private fun PokemonImage(
    modifier: Modifier = Modifier,
    pokemon: ShallowPokemon
) {
    Surface(
        tonalElevation = 3.dp,
        modifier = modifier,
//        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val placeholder = R.drawable.image_placeholder
        val imageUrl = pokemon.images.small ?: pokemon.images.large ?: ""

        val imageRequest = ImageRequest.Builder(context)
            .data(imageUrl)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = "Image Description",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@PreviewLightDark()
@Composable
fun AppBarPreview() {
    PokemonChallengeTheme() {
        AppBar()
    }
}

@PreviewLightDark()
@Composable
fun ContentPreview() {
    PokemonChallengeTheme() {
        Content(
            viewState = MutableStateFlow(
                MainViewModelState.Success(
                    currentPage = 0,
                    pokemonList = listOf(
                        ShallowPokemon(
                            id = "1",
                            name = "Bulbasaur",
                            images = PokemonImages()
                        ),
                        ShallowPokemon(
                            id = "2",
                            name = "Ivysaur",
                            images = PokemonImages()
                        ),
                        ShallowPokemon(
                            id = "3",
                            name = "Venusaur",
                            images = PokemonImages()
                        ),
                        ShallowPokemon(
                            id = "4",
                            name = "Charmander",
                            images = PokemonImages()
                        ),
                        ShallowPokemon(
                            id = "5",
                            name = "Charmeleon",
                            images = PokemonImages()
                        ),
                        ShallowPokemon(
                            id = "6",
                            name = "Charizard",
                            images = PokemonImages(),
                        )
                    )
                )
            )
        )
    }
}