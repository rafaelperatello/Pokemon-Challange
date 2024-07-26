package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun CardItem(
    color: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .aspectRatio(0.72f)
            .padding(3.dp)
            .clip(MaterialTheme.shapes.small),
        color = color,
        shape = MaterialTheme.shapes.small,
        content = content,
    )
}
