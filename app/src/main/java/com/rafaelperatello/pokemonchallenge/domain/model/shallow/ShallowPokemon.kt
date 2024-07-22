package com.rafaelperatello.pokemonchallenge.domain.model.shallow

internal data class ShallowPokemon(
    val id: String = "",
    val name: String = "",
    val number: String = "",
    val imageSmall: String? = null,
    val imageLarge: String? = null
)