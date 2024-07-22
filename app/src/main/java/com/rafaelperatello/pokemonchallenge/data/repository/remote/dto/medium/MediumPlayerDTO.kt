package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediumPlayerDTO(

    @SerialName("url")
    var url: String? = null
)
