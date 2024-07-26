package com.rafaelperatello.pokemonchallenge.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@PreviewLightDark()
@Composable
internal fun ContentPreviewPaginating() {
    PokemonChallengeTheme {
        val context = LocalContext.current

        val descriptionHttp = remember {
            mutableStateOf(
                context.getString(R.string.description_cache_size_kb, 20)
            )
        }

        val descriptionImage = remember {
            mutableStateOf(
                context.getString(R.string.description_cache_size_kb, 10)
            )
        }

        val settingItemDataList =
            listOf(
                SettingItemData(
                    label = stringResource(id = R.string.setting_database),
                    description = remember { mutableStateOf(context.getString(R.string.description_reset_pokemon_table)) },
                    buttonText = stringResource(id = R.string.reset),
                    onClick = { },
                ),
                SettingItemData(
                    label = stringResource(id = R.string.setting_network),
                    description = descriptionHttp,
                    buttonText = stringResource(id = R.string.clear),
                    onClick = { },
                ),
                SettingItemData(
                    label = stringResource(id = R.string.setting_image),
                    description = descriptionImage,
                    buttonText = stringResource(id = R.string.clear),
                    onClick = { },
                ),
            )

        SettingsContent(settingItemDataList)
    }
}
