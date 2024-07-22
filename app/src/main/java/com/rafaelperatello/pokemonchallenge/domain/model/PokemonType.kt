package com.rafaelperatello.pokemonchallenge.domain.model

internal val pokemonTypeToEnum = PokemonType.entries.associateBy { it.type }

/**
 * Types: https://bulbapedia.bulbagarden.net/wiki/Type_(TCG)
 */
internal enum class PokemonType(val type: String) {
    GRASS("Grass"),
    FIRE("Fire"),
    WATER("Water"),
    LIGHTNING("Lightning"),
    FIGHTING("Fighting"),
    PSYCHIC("Psychic"),
    COLORLESS("Colorless"),
    DARKNESS("Darkness"),
    METAL("Metal"),
    DRAGON("Dragon"),
    FAIRY("Fairy"),
    UNKNOWN("Unknown")
}
