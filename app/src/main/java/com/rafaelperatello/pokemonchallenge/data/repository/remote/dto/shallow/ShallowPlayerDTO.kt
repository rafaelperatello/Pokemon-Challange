package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowPlayerDTO(

    @SerialName("url")
    var url: String? = null
)
