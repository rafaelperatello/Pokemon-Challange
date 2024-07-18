package com.rafaelperatello.pokemonchallenge.data.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CardMarketDTO(

    @SerialName("url")
    var url: String? = null,

    @SerialName("updatedAt")
    var updatedAt: String? = null,

    @SerialName("prices")
    var prices: PricesDTO? = PricesDTO()
)