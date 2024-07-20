package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WeaknessesDTO(

    @SerialName("type")
    var type: String? = null,

    @SerialName("value")
    var value: String? = null
)