package com.rafaelperatello.pokemonchallenge.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import kotlinx.coroutines.flow.MutableStateFlow

private val pokemonList = buildList {
    repeat(10) {
        val pokemon = ShallowPokemon(
            id = it.toString(),
            name = "Pokemon $it"
        )

        add(pokemon)
    }
}

private val successState = MutableStateFlow(
    MainViewModelState.Success(
        currentPage = 0,
        pokemonList = pokemonList
    )
)

@Preview()
@Composable
fun ContentPreviewSmall() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = successState,
            listState = MutableStateFlow(ListState.IDLE),
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewMedium() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = successState,
            listState = MutableStateFlow(ListState.IDLE),
            gridStyleFlow = MutableStateFlow(GridStyle.MEDIUM)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLarge() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = successState,
            listState = MutableStateFlow(ListState.IDLE),
            gridStyleFlow = MutableStateFlow(GridStyle.LARGE)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewPaginating() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = successState,
            listState = MutableStateFlow(ListState.PAGINATING),
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewPaginationError() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(
                MainViewModelState.Success(
                    currentPage = 0,
                    pokemonList = pokemonList
                )
            ),
            listState = MutableStateFlow(ListState.ERROR),
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewError() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(MainViewModelState.Error),
            listState = MutableStateFlow(ListState.IDLE),
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLoading() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(MainViewModelState.Loading),
            listState = MutableStateFlow(ListState.IDLE),
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}