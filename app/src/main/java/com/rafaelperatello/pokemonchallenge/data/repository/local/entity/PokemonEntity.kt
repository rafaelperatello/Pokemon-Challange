package com.rafaelperatello.pokemonchallenge.data.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSubType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonSuperType
import com.rafaelperatello.pokemonchallenge.domain.model.PokemonType

@Entity(tableName = "pokemon")
internal data class PokemonEntity(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "number")
    val number: String,

    @ColumnInfo(name = "type")
    val types: Array<PokemonType>,

    @ColumnInfo(name = "super_type")
    val superType: PokemonSuperType,

    @ColumnInfo(name = "sub_type")
    val subType: Array<PokemonSubType>,

    @ColumnInfo(name = "image_small")
    val imageSmall: String?,

    @ColumnInfo(name = "image_large")
    val imageLarge: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "set_id")
    val setId: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PokemonEntity

        if (id != other.id) return false
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
        var result = id.hashCode()
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
