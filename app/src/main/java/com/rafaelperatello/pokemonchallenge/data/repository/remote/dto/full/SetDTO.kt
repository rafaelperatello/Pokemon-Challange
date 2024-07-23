package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetDTO(
    @SerialName("id")
    var id: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("series")
    var series: String? = null,
    @SerialName("printedTotal")
    var printedTotal: Int? = null,
    @SerialName("total")
    var total: Int? = null,
    @SerialName("legalities")
    var legalities: LegalitiesDTO? = LegalitiesDTO(),
    @SerialName("ptcgoCode")
    var ptcgoCode: String? = null,
    @SerialName("releaseDate")
    var releaseDate: String? = null,
    @SerialName("updatedAt")
    var updatedAt: String? = null,
    @SerialName("images")
    var images: ImagesDTO? = ImagesDTO(),
)
