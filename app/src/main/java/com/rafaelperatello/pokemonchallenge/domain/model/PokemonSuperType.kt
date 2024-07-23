package com.rafaelperatello.pokemonchallenge.domain.model

internal val PokemonSuperTypeToEnum = PokemonSuperType.entries.associateBy { it.type }

/**
 * Super types : https://api.pokemontcg.io/v2/supertypes
 */
internal enum class PokemonSuperType(val type: String) {
    ENERGY("Energy"),
    POKEMON("Pokémon"),
    TRAINER("Trainer"),
    UNKNOWN("Unknown")
}
