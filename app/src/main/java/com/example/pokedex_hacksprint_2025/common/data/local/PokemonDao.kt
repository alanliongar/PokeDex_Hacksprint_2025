package com.example.pokedex_hacksprint_2025.common.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("Select * From pokemonentity")
    suspend fun getAllPokemons(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPokemons(pokemons: List<PokemonEntity>)
}