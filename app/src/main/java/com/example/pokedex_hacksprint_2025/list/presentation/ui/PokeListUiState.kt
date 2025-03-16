package com.example.pokedex_hacksprint_2025.list.presentation.ui

data class PokeListUiState(
    val pokemonList: List<PokemonUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

data class PokemonUiData(
    val name: String,
    val image: String,
)