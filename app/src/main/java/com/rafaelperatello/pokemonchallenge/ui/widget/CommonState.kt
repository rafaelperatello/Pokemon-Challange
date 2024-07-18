package com.rafaelperatello.pokemonchallenge.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.rafaelperatello.pokemonchallenge.R
import com.rafaelperatello.pokemonchallenge.ui.theme.PokemonChallengeTheme

@Composable
fun LoadingWidget(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun ErrorWidget(
    modifier: Modifier = Modifier,
    errorDescription: String,
    errorAction: String,
    onRetryClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tagRetry = "tag_retry"

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            ) {
                append(errorDescription)
            }

            append("\n\n")

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            ) {
                pushStringAnnotation(tag = tagRetry, annotation = tagRetry)
                append(errorAction)
            }
        }

        ClickableText(
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { span ->
                        println("Clicked on ${span.item}")
                        onRetryClick()
                    }
            }
        )
    }
}

@PreviewLightDark()
@Composable
fun LoadingWidgetPreview() {
    PokemonChallengeTheme() {
        Surface() {
            LoadingWidget(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewLightDark()
@Composable
fun ErrorWidgetPreview() {
    PokemonChallengeTheme {
        Surface() {
            ErrorWidget(
                modifier = Modifier.fillMaxSize(),
                errorDescription = stringResource(R.string.error_loading_data),
                errorAction = stringResource(R.string.retry)
            )
        }
    }
}