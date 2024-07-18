package com.rafaelperatello.pokemonchallenge.data.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ReverseHolofoilDTO(

    @SerialName("low")
    var low: Double? = null,

    @SerialName("mid")
    var mid: Double? = null,

    @SerialName("high")
    var high: Double? = null,

    @SerialName("market")
    var market: Double? = null,

    @SerialName("directLow")
    var directLow: String? = null
)