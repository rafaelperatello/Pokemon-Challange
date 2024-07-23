package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PricesDTO(
    @SerialName("averageSellPrice")
    var averageSellPrice: Double? = null,
    @SerialName("lowPrice")
    var lowPrice: Double? = null,
    @SerialName("trendPrice")
    var trendPrice: Double? = null,
    @SerialName("germanProLow")
    var germanProLow: Int? = null,
    @SerialName("suggestedPrice")
    var suggestedPrice: Int? = null,
    @SerialName("reverseHoloSell")
    var reverseHoloSell: Double? = null,
    @SerialName("reverseHoloLow")
    var reverseHoloLow: Double? = null,
    @SerialName("reverseHoloTrend")
    var reverseHoloTrend: Double? = null,
    @SerialName("lowPriceExPlus")
    var lowPriceExPlus: Double? = null,
    @SerialName("avg1")
    var avg1: Double? = null,
    @SerialName("avg7")
    var avg7: Double? = null,
    @SerialName("avg30")
    var avg30: Double? = null,
    @SerialName("reverseHoloAvg1")
    var reverseHoloAvg1: Double? = null,
    @SerialName("reverseHoloAvg7")
    var reverseHoloAvg7: Double? = null,
    @SerialName("reverseHoloAvg30")
    var reverseHoloAvg30: Double? = null,
)
