package com.example.pokedex_hacksprint_2025.detail.presentation.ui

import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonDetail

data class PokemonDetailUiState(
    val pokemon: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)
