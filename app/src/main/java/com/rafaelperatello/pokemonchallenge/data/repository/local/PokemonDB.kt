package com.rafaelperatello.pokemonchallenge.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelperatello.pokemonchallenge.data.repository.local.PokemonDatabaseConstants.DATABASE_VERSION
import com.rafaelperatello.pokemonchallenge.data.repository.local.converters.PokemonSubTypeArrayConverter
import com.rafaelperatello.pokemonchallenge.data.repository.local.converters.PokemonTypeArrayConverter
import com.rafaelperatello.pokemonchallenge.data.repository.local.dao.PokemonDao
import com.rafaelperatello.pokemonchallenge.data.repository.local.entity.PokemonEntity

internal object PokemonDatabaseConstants {

    const val DATABASE_NAME = "pokemon.db"
    const val DATABASE_VERSION = 1

    object Tables {
        const val POKEMON = "pokemon"
    }
}

@Database(entities = [PokemonEntity::class], version = DATABASE_VERSION)
@TypeConverters(
    PokemonSubTypeArrayConverter::class,
    PokemonTypeArrayConverter::class
)
internal abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}
