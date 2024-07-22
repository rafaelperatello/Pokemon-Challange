package com.rafaelperatello.pokemonchallenge.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rafaelperatello.pokemonchallenge.data.repository.local.entity.PokemonEntity

@Dao
internal interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg users: PokemonEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(user: PokemonEntity)

    @Delete
    fun delete(user: PokemonEntity)

    @Query("SELECT * FROM pokemon")
    fun getAll(): List<PokemonEntity>
}
