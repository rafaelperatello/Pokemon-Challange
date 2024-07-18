package com.rafaelperatello.pokemonchallenge.domain.model.shallow

internal data class ShallowPokemon(
    var id: String,
    var name: String,
    var images: PokemonImages = PokemonImages()
)