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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokemonAppBar(
    title: String,
    colors: TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    action: State<AppBarAction?> = mutableStateOf(null),
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                letterSpacing = TextUnit(1f, TextUnitType.Sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = colors,
        actions = {
            action.value?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

internal data class AppBarAction(
    val imageVector: ImageVector,
    val onClick: () -> Unit,
)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark()
@Composable
fun AppBarPreviewDefault() {
    PokemonChallengeTheme {
        PokemonAppBar(
            title = "Pokémon Cards",
            action =
                mutableStateOf(
                    AppBarAction(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        onClick = {},
                    ),
                ),
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun AppBarPreviewLight() {
    PokemonChallengeTheme {
        PokemonAppBar(
            title = "Pokémon Cards",
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.tertiaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.surfaceBright,
                ),
            action =
                mutableStateOf(
                    AppBarAction(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        onClick = {},
                    ),
                ),
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun AppBarPreviewDark() {
    PokemonChallengeTheme(true) {
        PokemonAppBar(
            title = "Pokémon Cards",
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.tertiaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.surfaceBright,
                ),
            action =
                mutableStateOf(
                    AppBarAction(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        onClick = {},
                    ),
                ),
        )
    }
}
