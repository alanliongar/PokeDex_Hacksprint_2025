package com.example.pokedex_hacksprint_2025.detail.data.remote

import android.util.Log
import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonDetail
import com.example.pokedex_hacksprint_2025.detail.data.PokeDetailService

class PokemonDetailRemoteDataSource(
    private val pokeDetailService: PokeDetailService
) {
    suspend fun getPokemonDetails(name: String): Result<PokemonDetail> {
        return try {
            val result = pokeDetailService.getPokemon(name)

            if (result.isSuccessful) {
                val pokemon = result.body()

                val types = pokemon?.types?.map { it.type.name } ?: emptyList()
                val stats = pokemon?.stats?.map { it.baseStat } ?: emptyList()
                val weight = pokemon?.weight ?: 0
                val artworkUrl = pokemon?.sprites?.other?.officialArtwork?.frontDefault ?: ""

                Result.success(PokemonDetail(types, stats, weight, artworkUrl))
            } else {
                val errorMessage = result.errorBody()?.string() ?: "Unknown error"
                Log.d("PokemonDetailRemoteDataSource", "RequestError :: $errorMessage")
                Result.failure(Exception(errorMessage))
            }

        } catch (e: Exception) {
            Log.d("PokemonDetailRemoteDataSource", "RequestFailed :: ${e.message}")
            Result.failure(e)
        }
    }
}

