package com.rafaelperatello.pokemonchallenge.data.repository.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rafaelperatello.pokemonchallenge.data.repository.local.entity.PokemonEntity
import com.rafaelperatello.pokemonchallenge.data.repository.local.pojo.ShallowPokemonPojo

@Dao
internal interface PokemonDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnoring(pokemons: List<PokemonEntity>) : List<Long>

    // Todo check how to keep isFavorite when updating
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReplacing(pokemons: List<PokemonEntity>)

    // Todo check how to keep isFavorite when updating
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll()

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    suspend fun getAll(): List<PokemonEntity>

    @Query(
        """
        SELECT id, pokemon_id, name, number, image_small, image_large 
        FROM pokemon 
        ORDER BY id ASC
        LIMIT :limit 
        OFFSET :offset
        """,
    )
    suspend fun getAllShallow(
        limit: Int,
        offset: Int,
    ): List<ShallowPokemonPojo>

    @Query(
        """
        SELECT id, pokemon_id, name, number, image_small, image_large 
        FROM pokemon 
        ORDER BY id DESC
        """,
    )
    fun getAllPaging(): PagingSource<Int, ShallowPokemonPojo>

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun getCount(): Int
}
