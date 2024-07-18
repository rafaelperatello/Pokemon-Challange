package com.rafaelperatello.pokemonchallenge.domain.model.shallow

internal data class ShallowPokemonList(
    val data: List<ShallowPokemon> = arrayListOf(),
    val page: Int,
    val pageSize: Int,
    val count: Int,
    val totalCount: Int
)