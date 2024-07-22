package com.rafaelperatello.pokemonchallenge.domain.model

internal data class PokemonList<T>(
    val data: List<T> = arrayListOf(),
    val page: Int,
    val pageSize: Int,
    val count: Int,
    val totalCount: Int
)