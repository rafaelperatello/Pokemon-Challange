package com.rafaelperatello.pokemonchallenge.domain.model

internal val pokemonSubTypeToEnum = PokemonSubType.entries.associateBy { it.type }

/**
 * Subtypes: https://api.pokemontcg.io/v2/subtypes
 */
internal enum class PokemonSubType(val type: String) {
    ACE_SPEC("ACE SPEC"),
    ANCIENT("Ancient"),
    BABY("Baby"),
    BASIC("Basic"),
    BREAK("BREAK"),
    ETERNAMAX("Eternamax"),
    EX("EX"),
    EX_LOWERCASE("ex"),
    FUSION_STRIKE("Fusion Strike"),
    FUTURE("Future"),
    GOLDENROD_GAME_CORNER("Goldenrod Game Corner"),
    GX("GX"),
    ITEM("Item"),
    LEGEND("LEGEND"),
    LEVEL_UP("Level-Up"),
    MEGA("MEGA"),
    POKEMON_TOOL("Pokémon Tool"),
    POKEMON_TOOL_F("Pokémon Tool F"),
    PRIME("Prime"),
    PRISM_STAR("Prism Star"),
    RADIANT("Radiant"),
    RAPID_STRIKE("Rapid Strike"),
    RESTORED("Restored"),
    ROCKETS_SECRET_MACHINE("Rocket's Secret Machine"),
    SINGLE_STRIKE("Single Strike"),
    SP("SP"),
    SPECIAL("Special"),
    STADIUM("Stadium"),
    STAGE_1("Stage 1"),
    STAGE_2("Stage 2"),
    STAR("Star"),
    SUPPORTER("Supporter"),
    TAG_TEAM("TAG TEAM"),
    TEAM_PLASMA("Team Plasma"),
    TECHNICAL_MACHINE("Technical Machine"),
    TERA("Tera"),
    ULTRA_BEAST("Ultra Beast"),
    V("V"),
    VMAX("VMAX"),
    VSTAR("VSTAR"),
    V_UNION("V-UNION"),
    UNKNOWN("Unknown")
}
