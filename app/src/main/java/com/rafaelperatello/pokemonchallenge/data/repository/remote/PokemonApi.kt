package com.rafaelperatello.pokemonchallenge.data.repository.remote

import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.PokemonListDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full.FullPokemonDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.MediumPokemonDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.shallow.ShallowPokemonDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PokemonApi {

    @GET("cards?select=id,name,images")
    suspend fun getCardsShallow(
        @Query("page") currentPage: Int
    ): Response<PokemonListDTO<ShallowPokemonDTO>>

    @GET("cards?select=id,name,number,images,types,subtypes,supertype,set")
    suspend fun getCardsMedium(
        @Query("page") currentPage: Int
    ): Response<PokemonListDTO<MediumPokemonDTO>>

    @GET("cards/{id}")
    suspend fun getCard(
        @Path("id") id: String
    ): Response<FullPokemonDTO>
}