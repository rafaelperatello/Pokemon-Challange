package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediumImagesDTO(
    @SerialName("small")
    var small: String? = null,
    @SerialName("large")
    var large: String? = null,
)
