package com.example.pokedex_hacksprint_2025.list.data.remote

import android.accounts.NetworkErrorException
import com.example.pokedex_hacksprint_2025.common.data.model.Pokemon
import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonListResponse

class PokemonListRemoteDataSource(
    private val pokeListService: PokeListService
) {
    suspend fun getPokemonList(): Result<List<Pokemon>?> {
        return try {
            val response =
                pokeListService.getPokemonList().execute() // Executa de forma síncrona
            if (response.isSuccessful) {
                val pokemons = response.body()?.results?.map {
                    Pokemon(
                        name = it.name,
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                                it.url.trimEnd('/').split('/').last() +
                                ".png"
                    )
                }
                Result.success(pokemons)
            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}