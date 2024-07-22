package com.rafaelperatello.pokemonchallenge.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
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

private fun buildLoadStates(
    refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
    prepend: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
    append: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
): LoadStates {
    return LoadStates(
        refresh = refresh,
        prepend = prepend,
        append = append
    )
}

private fun fakeDataFlow(
    sourceLoadStates: LoadStates = buildLoadStates(),
    mediatorLoadStates: LoadStates = buildLoadStates()
): MutableStateFlow<PagingData<ShallowPokemon>> {
    val pagingData: PagingData<ShallowPokemon> = PagingData.from(
        data = pokemonList,
        sourceLoadStates = sourceLoadStates,
        mediatorLoadStates = mediatorLoadStates
    )
    return MutableStateFlow(pagingData)
}

@Preview()
@Composable
fun ContentPreviewSmall() {
    PokemonChallengeTheme {
        val listState = fakeDataFlow().collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewMedium() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow().collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.MEDIUM)
        )
    }
}

@Preview()
@Composable
fun ContentPreviewLarge() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow().collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.LARGE)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewPaginating() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow(
            mediatorLoadStates = buildLoadStates(
                append = LoadState.Loading
            )
        ).collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewPaginationError() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow(
            mediatorLoadStates = buildLoadStates(
                append = LoadState.Error(IllegalStateException("error"))
            )
        ).collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewError() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow(
            mediatorLoadStates = buildLoadStates(
                refresh = LoadState.Loading
            )
        ).collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreviewLoading() {
    PokemonChallengeTheme() {
        val listState = fakeDataFlow(
            mediatorLoadStates = buildLoadStates(
                refresh = LoadState.Error(IllegalStateException("error"))
            )
        ).collectAsLazyPagingItems()
        HomeContent(
            listState = listState,
            gridStyleFlow = MutableStateFlow(GridStyle.SMALL)
        )
    }
}
