package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImagesDTO(

    @SerialName("small")
    var small: String? = null,

    @SerialName("large")
    var large: String? = null
)