package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.coroutines.Dispatchers

@Composable
internal fun PokemonImage(
    filterQuality: FilterQuality = FilterQuality.Low,
    pokemon: ShallowPokemon,
    position: Int,
    onPokemonClick: (ShallowPokemon) -> Unit
) {
    val context = LocalContext.current
    val imageUrl = pokemon.imageSmall ?: ""

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onPokemonClick(pokemon)
            },
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            filterQuality = filterQuality
        )

        Text(
            text = (position + 1).toString(),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-4).dp, (-4).dp)
                .background(
                    color = Color(0xFFFF0000),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp, 3.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
