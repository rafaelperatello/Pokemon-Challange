package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowImagesDTO(

    @SerialName("small")
    var small: String? = null,

    @SerialName("large")
    var large: String? = null
)