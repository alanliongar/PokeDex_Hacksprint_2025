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
            val result = local.getPokemonList()
            if (result.isEmpty()) {
                val remoteData = remote.getPokemonList()
                if (remoteData.isSuccess) {
                    val pokemonsRemote = remoteData.getOrNull()?.map {
                        it.copy(name = it.name.replaceFirstChar { c -> c.uppercaseChar() })
                    } ?: emptyList()
                    local.updateLocalPokemons(pokemonsRemote)
                } else {
                    return@getPokemonList Result.failure(Exception("api failure and empty data"))
                }
            }
            Result.success(local.getPokemonList())
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}