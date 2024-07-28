package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CardItem(
    modifier: Modifier = Modifier,
    elevation: CardElevation,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    content: @Composable () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(0.72f)
            .padding(3.dp),
        shape = MaterialTheme.shapes.small,
        colors = colors,
        elevation = elevation,
    ) {
        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}
