package com.rafaelperatello.pokemonchallenge.data.repository.local.converters

import androidx.room.TypeConverter
import com.rafaelperatello.pokemonchallenge.BuildConfig
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonType
import com.rafaelperatello.pokemonchallenge.domain.model.pokemonTypeToEnum

internal class PokemonTypeArrayConverter {

    @TypeConverter
    fun fromPokemonSubTypeArray(value: Array<PokemonType>): String {
        return value.map { it.name }.toTypedArray().joinToString(",")
    }

    @TypeConverter
    fun fromStringArray(array: String?): Array<PokemonType> {
        return array?.split(",")?.map {
            pokemonTypeToEnum[it]
                ?: run {
                    if (BuildConfig.DEBUG) {
                        throw IllegalArgumentException("Unknown PokemonSubType: $it")
                    }

                    PokemonType.UNKNOWN
                }
        }?.toTypedArray() ?: emptyArray()
    }
}
