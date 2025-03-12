package com.example.pokedex_hacksprint_2025.list.presentation

import com.example.pokedex_hacksprint_2025.common.model.PokemonListResponse

data class PokeListUiState(
    val list: PokemonListResponse = PokemonListResponse(results = emptyList()),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Something went wrong!",
)