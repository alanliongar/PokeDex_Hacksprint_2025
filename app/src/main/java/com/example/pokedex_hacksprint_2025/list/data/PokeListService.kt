package com.example.pokedex_hacksprint_2025.list.data

import com.example.pokedex_hacksprint_2025.common.model.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeListService {
    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit:  Int =10): Call<PokemonListResponse>
}