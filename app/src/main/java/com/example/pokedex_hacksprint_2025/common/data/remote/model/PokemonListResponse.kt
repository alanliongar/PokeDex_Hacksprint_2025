package com.example.pokedex_hacksprint_2025.common.data.remote.model

import com.google.gson.annotations.SerializedName


data class PokemonListResponse(
//Alan - Esse é o dataclass que faz o store da resposta da API.
    val results: List<PokemonItem>?,
)

data class PokemonItem( //Alan - esse aqui é o DTO da lista
    val name: String,
    var url: String
)

// Modelo de resposta principal da segunda requisicao
data class PokemonApiResult(
    val types: List<TypeSlot>,
    val stats: List<Stat>,
    @SerializedName("base_experience") val baseExp: Int = 0,
    val height: Int,
    val weight: Int,
    val sprites: Sprites  // Adicionando o campo sprites
)

// Representa cada tipo do Pokémon, com slot e tipo
data class TypeSlot(
    val slot: Int,
    val type: TypeDetails
)

// Detalhes do tipo (name e URL)
data class TypeDetails(
    val name: String,
    val url: String
)

// Estatísticas do Pokémon (baseStat, effort e stat.name)
data class Stat(
    @SerializedName("base_stat") val baseStat: Int,
    val effort: Int,
    val stat: StatDetails
)

// Detalhes da estatística (nome e URL)
data class StatDetails(
    val name: String,
    val url: String
)

// Definindo o campo sprites com o "other" e o "official-artwork"
data class Sprites(
    val other: OtherSprites
)

// Subclasse para representar o "other" e o "official-artwork"
data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
)

// Definindo o objeto "official-artwork"
data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String  // Caminho para a URL da imagem
)

data class PokemonDetail(
    val types: List<String> = emptyList(),
    val stats: List<Int> = emptyList(),
    @SerializedName("base_experience") val baseExp: Int = 0,
    val weight: Int? = 0,
    val height: Int? = 0,
    val art: String? = ""
)
