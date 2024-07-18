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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
                        viewState = viewModel.state,
                        onRetryClick = { viewModel.onRetryClick() }
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
    onRetryClick: () -> Unit = {}
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
                Error(onRetryClick)
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
private fun Error(
    onRetryClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tagRetry = "tag_retry"

        val annotatedString = buildAnnotatedString {
            append(stringResource(R.string.error_loading_data))

            append("\n\n")

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            ) {
                pushStringAnnotation(tag = tagRetry, annotation = tagRetry)
                append(stringResource(R.string.retry))
            }
        }


        ClickableText(
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { span ->
                        println("Clicked on ${span.item}")
                        onRetryClick()
                    }
            }
        )
    }
}

@Composable
private fun PokemonGrid(
    modifier: Modifier = Modifier,
    pokemonList: List<ShallowPokemon>
) {
    LazyVerticalGrid(
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
        shape = MaterialTheme.shapes.small
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
            contentDescription = stringResource(R.string.title_main),
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

@Preview()
@Composable
fun ContentPreviewError() {
    PokemonChallengeTheme() {
        Content(
            viewState = MutableStateFlow(MainViewModelState.Error)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLoading() {
    PokemonChallengeTheme() {
        Content(
            viewState = MutableStateFlow(MainViewModelState.Loading)
        )
    }
}