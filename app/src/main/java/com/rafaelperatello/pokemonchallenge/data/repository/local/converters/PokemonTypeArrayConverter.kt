package com.rafaelperatello.pokemonchallenge.data.repository.local.converters

import androidx.room.TypeConverter
import com.rafaelperatello.pokemonchallenge.BuildConfig
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonTypeToEnum

internal class PokemonTypeArrayConverter {
    @TypeConverter
    fun fromPokemonSubTypeArray(value: Array<PokemonType>): String =
        value
            .map { it.type }
            .toTypedArray()
            .joinToString(",")

    @TypeConverter
    fun fromStringArray(array: String?): Array<PokemonType> =
        array
            ?.takeIf { it.isNotBlank() }
            ?.split(",")
            ?.map {
                PokemonTypeToEnum[it]
                    ?: run {
                        if (BuildConfig.DEBUG) {
                            throw IllegalArgumentException("Unknown PokemonSubType: $it")
                        }

                        PokemonType.UNKNOWN
                    }
            }?.toTypedArray() ?: emptyArray()
}
