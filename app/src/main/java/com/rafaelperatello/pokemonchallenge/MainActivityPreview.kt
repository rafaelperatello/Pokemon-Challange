package com.rafaelperatello.pokemonchallenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@Preview()
@Composable
fun ContentPreviewLight() {
    val isDarkTheme = false
    PokemonChallengeTheme(
        isDarkTheme,
    ) {
        PokemonScaffold(
            navController = rememberNavController(),
            currentRoute = remember {
                mutableStateOf(MainRoutes.Favorites)
            },
            body = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Favorites")
                    }
                }
            },
            isDarkTheme = isDarkTheme,
        )
    }
}

@Preview()
@Composable
fun ContentPreviewDark() {
    val isDarkTheme = true
    PokemonChallengeTheme(
        isDarkTheme
    ) {
        PokemonScaffold(
            navController = rememberNavController(),
            currentRoute = remember {
                mutableStateOf(MainRoutes.Favorites)
            },
            body = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Favorites")
                    }
                }
            },
            isDarkTheme = isDarkTheme,
        )
    }
}

@Preview()
@Composable
fun MainBottomBarPreviewLight() {
    PokemonChallengeTheme {
        MainBottomBar(
            currentRoute = remember { mutableStateOf(MainRoutes.Favorites) },
        )
    }
}
