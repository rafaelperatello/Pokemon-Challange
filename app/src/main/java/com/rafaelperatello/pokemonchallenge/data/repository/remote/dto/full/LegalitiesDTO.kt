package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LegalitiesDTO(
    @SerialName("unlimited")
    var unlimited: String? = null,
)
