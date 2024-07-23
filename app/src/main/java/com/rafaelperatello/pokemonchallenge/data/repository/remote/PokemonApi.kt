package com.rafaelperatello.pokemonchallenge.data.repository.remote

import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.PokemonListDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full.FullPokemonDTO
import com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.medium.MediumPokemonDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal object PokemonApiConstants {
    const val BASE_URL: String = "https://api.pokemontcg.io/v2/"
}

internal interface PokemonApi {
    @GET("cards?select=id,name,number,images,types,subtypes,supertype,set")
    suspend fun getCards(
        @Query("page") currentPage: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<PokemonListDTO<MediumPokemonDTO>>

    @GET("cards/{id}")
    suspend fun getCard(
        @Path("id") id: String,
    ): Response<FullPokemonDTO>
}
