package com.rafaelperatello.pokemonchallenge.data.remote

import com.rafaelperatello.pokemonchallenge.data.remote.dto.full.FullPokemonDTO
import com.rafaelperatello.pokemonchallenge.data.remote.dto.shallow.ShallowPokemonListDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PokemonApi {

    @GET("cards?select=id,name,images")
    suspend fun getList(
        @Query("page") currentPage: Int
    ): Response<ShallowPokemonListDTO>

    @GET("cards/{id}")
    suspend fun getCard(
        @Path("id") id: String
    ): Response<FullPokemonDTO>
}