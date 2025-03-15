package com.example.pokedex_hacksprint_2025.detail.data

import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeDetailService {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Response<PokemonApiResult>
}