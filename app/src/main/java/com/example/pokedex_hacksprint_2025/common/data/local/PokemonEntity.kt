package com.example.pokedex_hacksprint_2025.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey
    val name: String,
    val image: String
)
