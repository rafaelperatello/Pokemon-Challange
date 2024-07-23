package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto

import com.rafaelperatello.pokemonchallenge.domain.model.PokemonList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonListDTO<T>(
    @SerialName("data")
    var data: ArrayList<T> = arrayListOf(),
    @SerialName("page")
    var page: Int,
    @SerialName("pageSize")
    var pageSize: Int,
    @SerialName("count")
    var count: Int,
    @SerialName("totalCount")
    var totalCount: Int,
)

internal fun <T, R> PokemonListDTO<T>.toTypedDTO(mapper: (T) -> R) =
    PokemonList<R>(
        data = data.map { mapper(it) },
        page = page,
        pageSize = pageSize,
        count = count,
        totalCount = totalCount,
    )
