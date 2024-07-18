package com.rafaelperatello.pokemonchallenge.data.remote.dto.shallow

import com.rafaelperatello.pokemonchallenge.domain.model.pokemon.shallow.PokemonImages
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImagesDTO(

    @SerialName("small")
    var small: String? = null,

    @SerialName("large")
    var large: String? = null
)

internal fun ImagesDTO.toImages() = PokemonImages(
    small = small,
    large = large
)