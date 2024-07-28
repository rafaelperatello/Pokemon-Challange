package com.rafaelperatello.pokemonchallenge.ui.widget

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.coroutines.Dispatchers

@Composable
internal fun PokemonImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    imageUrlLowRes: String = "",
    imageUrlHighRes: String? = null,
    filterQuality: FilterQuality = FilterQuality.Low,
    showLabel: Boolean = true,
    pokemon: ShallowPokemon,
) {
    Box(
        modifier = modifier
    ) {
        val context = LocalContext.current

        val imageUrl = imageUrlHighRes ?: imageUrlLowRes
        val imageRequest = buildImageRequest(context, imageUrl)

        SubcomposeAsyncImage(
            modifier = imageModifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small),
            model = imageRequest,
            contentDescription = pokemon.name,
            contentScale = ContentScale.FillBounds,
            filterQuality = filterQuality,
        ) {
            val state = painter.state
            Log.d("PokemonImage", "state: $state")
            if (imageUrlHighRes != null &&
                state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error
            ) {
                Log.d("PokemonImage", "loading high res image")
                AsyncImage(
                    modifier = imageModifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small),
                    model = buildImageRequest(context, imageUrlLowRes),
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.FillBounds,
                    filterQuality = filterQuality,
                )
            } else {
                SubcomposeAsyncImageContent()
            }
        }

        if (showLabel) {
            Text(
                text = pokemon.id.toString(),
                color = Color.White,
                modifier = labelModifier
                    .align(Alignment.BottomEnd)
                    .offset((-4).dp, (-4).dp)
                    .background(
                        color = Color(0xFFFF0000),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(8.dp, 3.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

private fun buildImageRequest(
    context: Context,
    imageUrl: String,
) =
    ImageRequest
        .Builder(context)
        .data(imageUrl)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
