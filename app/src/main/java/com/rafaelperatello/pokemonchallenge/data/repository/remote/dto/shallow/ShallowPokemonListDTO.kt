package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow

import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemonList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowPokemonListDTO(

    @SerialName("data")
    var data: ArrayList<ShallowPokemonDTO> = arrayListOf(),

    @SerialName("page")
    var page: Int,

    @SerialName("pageSize")
    var pageSize: Int,

    @SerialName("count")
    var count: Int,

    @SerialName("totalCount")
    var totalCount: Int
)

internal fun ShallowPokemonListDTO.toShallowPokemonList() = ShallowPokemonList(
    data = data.map { it.toShallowPokemon() },
    page = page,
    pageSize = pageSize,
    count = count,
    totalCount = totalCount
)