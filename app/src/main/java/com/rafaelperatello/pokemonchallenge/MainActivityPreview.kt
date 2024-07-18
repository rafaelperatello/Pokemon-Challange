package com.rafaelperatello.pokemonchallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme
import kotlinx.coroutines.flow.MutableStateFlow

@PreviewLightDark()
@Composable
fun ContentPreview() {
    PokemonChallengeTheme() {
        Content(
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
        Content(
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
        Content(
            viewState = MutableStateFlow(MainViewModelState.Error),
            listState = MutableStateFlow(ListState.IDLE)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLoading() {
    PokemonChallengeTheme() {
        Content(
            viewState = MutableStateFlow(MainViewModelState.Loading),
            listState = MutableStateFlow(ListState.IDLE)
        )
    }
}