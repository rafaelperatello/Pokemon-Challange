package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AttacksDTO(

    @SerialName("name")
    var name: String? = null,

    @SerialName("cost")
    var cost: ArrayList<String> = arrayListOf(),

    @SerialName("convertedEnergyCost")
    var convertedEnergyCost: Int? = null,

    @SerialName("damage")
    var damage: String? = null,

    @SerialName("text")
    var text: String? = null
)