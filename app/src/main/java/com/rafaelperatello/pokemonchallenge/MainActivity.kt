package com.rafaelperatello.pokemonchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

class MainActivity : ComponentActivity() {
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
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.title_main)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Lorem ipsum dolor sit amet..."
        )
    }
}

@PreviewLightDark()
@Composable
fun ContentPreview() {
    PokemonChallengeTheme(dynamicColor = false) {
        Content()
    }
}

@PreviewLightDark()
@Composable
fun AppBarPreview() {
    PokemonChallengeTheme(dynamicColor = false) {
        AppBar()
    }
}