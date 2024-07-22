package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediumSetDTO(

    @SerialName("id")
    var id: String? = null
)