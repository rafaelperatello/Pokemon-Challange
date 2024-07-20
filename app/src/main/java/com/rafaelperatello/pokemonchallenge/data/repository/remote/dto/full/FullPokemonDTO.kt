package com.rafaelperatello.pokemonchallenge.data.repository.remote.dto.full

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FullPokemonDTO(

    @SerialName("id")
    var id: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("supertype")
    var supertype: String? = null,

    @SerialName("subtypes")
    var subtypes: String? = null,

    @SerialName("hp")
    var hp: String? = null,

    @SerialName("types")
    var types: ArrayList<String> = arrayListOf(),

    @SerialName("evolvesFrom")
    var evolvesFrom: String? = null,

    @SerialName("attacks")
    var attacks: ArrayList<AttacksDTO> = arrayListOf(),

    @SerialName("weaknesses")
    var weaknesses: ArrayList<WeaknessesDTO> = arrayListOf(),

    @SerialName("resistances")
    var resistances: ArrayList<ResistancesDTO> = arrayListOf(),

    @SerialName("retreatCost")
    var retreatCost: ArrayList<String> = arrayListOf(),

    @SerialName("convertedRetreatCost")
    var convertedRetreatCost: Int? = null,

    @SerialName("set")
    var setDTO: SetDTO? = SetDTO(),

    @SerialName("number")
    var number: String? = null,

    @SerialName("artist")
    var artist: String? = null,

    @SerialName("rarity")
    var rarity: String? = null,

    @SerialName("flavorText")
    var flavorText: String? = null,

    @SerialName("nationalPokedexNumbers")
    var nationalPokedexNumbers: ArrayList<Int> = arrayListOf(),

    @SerialName("legalities")
    var legalitiesDTO: LegalitiesDTO? = LegalitiesDTO(),

    @SerialName("images")
    var imagesDTO: ImagesDTO? = ImagesDTO(),

    @SerialName("tcgplayer")
    var tcgplayer: TcgPlayerDTO? = TcgPlayerDTO(),

    @SerialName("cardmarket")
    var cardmarketDTO: CardMarketDTO? = CardMarketDTO()
)