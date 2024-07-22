package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium

import com.rafaelperatello.pokemonchallenge.BuildConfig
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSubType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSuperType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonType
import com.rafaelperatello.pokemonchallenge.domain.model.medium.MediumPokemon
import com.rafaelperatello.pokemonchallenge.domain.model.pokemonSubTypeToEnum
import com.rafaelperatello.pokemonchallenge.domain.model.pokemonSuperTypeToEnum
import com.rafaelperatello.pokemonchallenge.domain.model.pokemonTypeToEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediumPokemonDTO(

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
    var setDTO: MediumSetDTO? = MediumSetDTO(),

    @SerialName("tcgplayer")
    var tcgplayer: MediumPlayerDTO? = MediumPlayerDTO(),

    @SerialName("images")
    var images: MediumImagesDTO = MediumImagesDTO()
)

internal fun MediumPokemonDTO.toMediumPokemon() = MediumPokemon(
    id = id,
    name = name,
    number = number,
    imageSmall = images.small,
    imageLarge = images.large,
    url = tcgplayer?.url,
    isFavorite = false,
    setId = setDTO?.id ?: kotlin.run {
        if (BuildConfig.DEBUG) {
            throw IllegalStateException("Unknown set for pokemon")
        }

        ""
    },
    types = types.map {
        pokemonTypeToEnum[it] ?: kotlin.run {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("Unknown type for pokemon $types")
            }

            PokemonType.UNKNOWN
        }
    }.toTypedArray(),
    superType = supertype?.let { superTypeString ->
        pokemonSuperTypeToEnum[superTypeString]
    } ?: kotlin.run {
        if (BuildConfig.DEBUG) {
            throw IllegalStateException("Unknown super type for pokemon $supertype")
        }

        PokemonSuperType.UNKNOWN
    },
    subType = subtypes.map {
        pokemonSubTypeToEnum[it] ?: kotlin.run {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("Unknown sub type for pokemon $subtypes")
            }

            PokemonSubType.UNKNOWN
        }
    }.toTypedArray()
)
