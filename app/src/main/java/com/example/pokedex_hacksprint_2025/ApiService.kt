package com.example.pokedex_hacksprint_2025

import retrofit2.Call
import retrofit2.http.GET

interface PokemonApi {

    @GET("pokemon")
    fun getPokemonList(): Call<PokemonListResponse>
}