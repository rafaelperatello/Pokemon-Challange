package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow

import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShallowPokemonDTO(

    @SerialName("id")
    var id: String,

    @SerialName("name")
    var name: String,

    @SerialName("number")
    val number: String,

    @SerialName("types")
    var types: ArrayList<String> = arrayListOf(),

    @SerialName("supertype")
    var supertype: String? = null,

    @SerialName("subtypes")
    var subtypes: ArrayList<String> = arrayListOf(),

    @SerialName("set")
    var setDTO: ShallowSetDTO? = ShallowSetDTO(),

    @SerialName("tcgplayer")
    var tcgplayer: ShallowPlayerDTO? = ShallowPlayerDTO(),

    @SerialName("images")
    var images: ShallowImagesDTO = ShallowImagesDTO()
)

internal fun ShallowPokemonDTO.toShallowPokemon() = ShallowPokemon(
    id = id,
    name = name,
    number = number,
    imageSmall = images.small,
    imageLarge = images.large,
)