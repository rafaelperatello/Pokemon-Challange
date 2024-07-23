package com.rafaelperatello.pokemonchallenge.domain.model.medium

import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSubType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSuperType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonType

internal data class MediumPokemon(
    val id: Int = 0,
    val pokemonId: String = "",
    val name: String = "",
    val number: String = "",
    val types: Array<PokemonType> = arrayOf(PokemonType.UNKNOWN),
    val superType: PokemonSuperType = PokemonSuperType.UNKNOWN,
    val subType: Array<PokemonSubType> = arrayOf(PokemonSubType.UNKNOWN),
    val imageSmall: String? = null,
    val imageLarge: String? = null,
    val url: String? = null,
    val setId: String = "",
    val isFavorite: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediumPokemon

        if (id != other.id) return false
        if (pokemonId != other.pokemonId) return false
        if (name != other.name) return false
        if (number != other.number) return false
        if (!types.contentEquals(other.types)) return false
        if (superType != other.superType) return false
        if (!subType.contentEquals(other.subType)) return false
        if (imageSmall != other.imageSmall) return false
        if (imageLarge != other.imageLarge) return false
        if (url != other.url) return false
        if (setId != other.setId) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + pokemonId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + types.contentHashCode()
        result = 31 * result + superType.hashCode()
        result = 31 * result + subType.contentHashCode()
        result = 31 * result + (imageSmall?.hashCode() ?: 0)
        result = 31 * result + (imageLarge?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + setId.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}