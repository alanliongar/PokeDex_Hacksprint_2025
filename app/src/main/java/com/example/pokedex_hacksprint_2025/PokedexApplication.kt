package com.example.pokedex_hacksprint_2025

import android.app.Application
import androidx.room.Room
import com.example.pokedex_hacksprint_2025.common.data.remote.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.data.local.PokedexDataBase
import com.example.pokedex_hacksprint_2025.list.data.PokemonListRepository
import com.example.pokedex_hacksprint_2025.list.data.local.PokemonListLocalDataSource
import com.example.pokedex_hacksprint_2025.list.data.remote.PokeListService
import com.example.pokedex_hacksprint_2025.list.data.remote.PokemonListRemoteDataSource


class PokedexApplication : Application() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PokedexDataBase::class.java, "database-pokedex_00"
        ).build()
    }
    private val pokemonListService by lazy { RetroFitClient.retrofit.create(PokeListService::class.java) }

    private val localDataSource: PokemonListLocalDataSource by lazy { PokemonListLocalDataSource(db.getPokemonDao()) }

    private val remoteDataSource: PokemonListRemoteDataSource by lazy {
        PokemonListRemoteDataSource(
            pokemonListService
        )
    }
    val repository by lazy {
        PokemonListRepository(
            local = localDataSource,
            remote = remoteDataSource
        )
    }
}