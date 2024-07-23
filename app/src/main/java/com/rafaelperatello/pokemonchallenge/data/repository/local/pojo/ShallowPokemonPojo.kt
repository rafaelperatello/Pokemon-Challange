package com.rafaelperatello.pokemonchallenge.data.repository.local.pojo

import androidx.room.ColumnInfo
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon

internal data class ShallowPokemonPojo(

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "pokemon_id")
    val pokemonId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "number")
    val number: String,

    @ColumnInfo(name = "image_small")
    val imageSmall: String?,

    @ColumnInfo(name = "image_large")
    val imageLarge: String?,
)

internal fun ShallowPokemonPojo.toShallowPokemon(): ShallowPokemon {
    return ShallowPokemon(
        id = id,
        pokemonId = pokemonId,
        name = name,
        number = number,
        imageSmall = imageSmall,
        imageLarge = imageLarge,
    )
}
