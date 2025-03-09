package com.example.pokedex_hacksprint_2025

import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit:  Int = 1025): Call<PokemonListResponse>

    @GET("pokemon/{number}")
    fun getPokemon(@Path("number") number: Int): Call<PokemonApiResult>

}