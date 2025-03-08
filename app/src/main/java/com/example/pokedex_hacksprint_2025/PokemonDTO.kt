package com.example.pokedex_hacksprint_2025;

@kotlinx.serialization.Serializable
data class PokemonItem(
        val name: String,
        var url: String
){

}

//https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png