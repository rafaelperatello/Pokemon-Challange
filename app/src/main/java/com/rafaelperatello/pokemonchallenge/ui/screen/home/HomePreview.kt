package com.rafaelperatello.pokemonchallenge.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import kotlinx.coroutines.flow.MutableStateFlow

@PreviewLightDark()
@Composable
fun ContentPreview() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(
                MainViewModelState.Success(
                    currentPage = 0,
                    pokemonList = buildList {
                        repeat(10) {
                            val pokemon = ShallowPokemon(
                                id = it.toString(),
                                name = "Pokemon $it"
                            )

                            add(pokemon)
                        }
                    }
                )
            ),
            listState = MutableStateFlow(ListState.PAGINATING)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreview2() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(
                MainViewModelState.Success(
                    currentPage = 0,
                    pokemonList = buildList {
                        repeat(10) {
                            val pokemon = ShallowPokemon(
                                id = it.toString(),
                                name = "Pokemon $it"
                            )

                            add(pokemon)
                        }
                    }
                )
            ),
            listState = MutableStateFlow(ListState.ERROR)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewError() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(MainViewModelState.Error),
            listState = MutableStateFlow(ListState.IDLE)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLoading() {
    PokemonChallengeTheme() {
        HomeContent(
            viewState = MutableStateFlow(MainViewModelState.Loading),
            listState = MutableStateFlow(ListState.IDLE)
        )
    }
}