package com.example.pokedex_hacksprint_2025.list.data

import com.example.pokedex_hacksprint_2025.common.data.model.Pokemon
import com.example.pokedex_hacksprint_2025.list.data.local.PokemonListLocalDataSource
import com.example.pokedex_hacksprint_2025.list.data.remote.PokemonListRemoteDataSource

class PokemonListRepository(
    private val local: PokemonListLocalDataSource,
    private val remote: PokemonListRemoteDataSource,
) {
    suspend fun getPokemonList(): Result<List<Pokemon>?> {
        return try {
            val result = remote.getPokemonList()
            if (result.isSuccess) {
                val pokemonsRemote = result.getOrNull() ?: emptyList()
                if (pokemonsRemote.isNotEmpty()) {
                    local.updateLocalPokemons(pokemonsRemote)
                }
            } else {
                val localData = local.getPokemonList()
                if (localData.isEmpty()) {
                    return@getPokemonList result
                } else {
                    return@getPokemonList Result.success(localData)
                }
            }
            Result.success(local.getPokemonList())
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}