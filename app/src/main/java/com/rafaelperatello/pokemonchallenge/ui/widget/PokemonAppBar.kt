package com.rafaelperatello.pokemonchallenge.ui.widget

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokemonAppBar(
    title: String,
    action: State<AppBarAction?> = mutableStateOf(null),
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            action.value?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

internal data class AppBarAction(
    val imageVector: ImageVector,
    val onClick: () -> Unit
)

@SuppressLint("UnrememberedMutableState")
@PreviewLightDark()
@Composable
fun AppBarPreview() {
    PokemonChallengeTheme() {
        PokemonAppBar(
            title = "Pokemon Challenge",
            action = mutableStateOf(
                AppBarAction(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    onClick = {}
                )
            )
        )
    }
}
