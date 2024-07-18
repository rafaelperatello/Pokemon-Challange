package com.rafaelperatello.pokemonchallenge.data.remote.dto.shallow

import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowPokemonDTO(

    @SerialName("id")
    var id: String,

    @SerialName("name")
    var name: String,

    @SerialName("images")
    var images: ImagesDTO = ImagesDTO()
)

internal fun ShallowPokemonDTO.toShallowPokemon() = ShallowPokemon(
    id = id,
    name = name,
    images = images.toImages()
)