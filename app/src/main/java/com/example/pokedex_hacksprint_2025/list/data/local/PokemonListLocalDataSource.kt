package com.example.pokedex_hacksprint_2025.list.data.local

import com.example.pokedex_hacksprint_2025.common.data.local.PokemonDao
import com.example.pokedex_hacksprint_2025.common.data.local.PokemonEntity
import com.example.pokedex_hacksprint_2025.common.data.model.Pokemon

class PokemonListLocalDataSource(
    private val dao: PokemonDao,
) {
    suspend fun getPokemonList(): List<Pokemon> {
        val pokemonEntities = dao.getAllPokemons()
        return pokemonEntities.map {
            Pokemon(
                name = it.name,
                image = it.image
            )
        }
    }

    suspend fun updateLocalPokemons(pokemons: List<Pokemon>) {
        val pokEntities = pokemons.map {
            PokemonEntity(
                name = it.name,
                image = it.image
            )
        }
        dao.insertAllPokemons(pokemons = pokEntities)
    }
}