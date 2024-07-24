package com.rafaelperatello.pokemonchallenge.data.repository.local.converters

import androidx.room.TypeConverter
import com.rafaelperatello.pokemonchallenge.BuildConfig
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSubType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSubTypeToEnum

internal class PokemonSubTypeArrayConverter {
    @TypeConverter
    fun fromPokemonSubTypeArray(value: Array<PokemonSubType>): String =
        value
            .map { it.type }
            .toTypedArray()
            .joinToString(",")

    @TypeConverter
    fun fromStringArray(array: String?): Array<PokemonSubType> =
        array
            ?.split(",")
            ?.map {
                PokemonSubTypeToEnum[it]
                    ?: run {
                        if (BuildConfig.DEBUG) {
                            throw IllegalArgumentException("Unknown PokemonSubType: $it")
                        }

                        PokemonSubType.UNKNOWN
                    }
            }?.toTypedArray() ?: emptyArray()
}
