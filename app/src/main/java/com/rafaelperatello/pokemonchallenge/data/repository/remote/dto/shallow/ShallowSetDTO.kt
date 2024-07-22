package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowSetDTO(

    @SerialName("id")
    var id: String? = null
)