package com.example.pokedex_hacksprint_2025.list.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.PokedexApplication
import com.example.pokedex_hacksprint_2025.list.data.PokemonListRepository
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokeListUiState
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokemonUiData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokeListViewModel(
    private val repository: PokemonListRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _pokemonListUiState =
        MutableStateFlow<PokeListUiState>(PokeListUiState())
    val pokemonListUiState: StateFlow<PokeListUiState> = _pokemonListUiState

    private val _selectedPokemons = MutableStateFlow<List<PokemonUiData>>(emptyList())
    val selectedPokemons: StateFlow<List<PokemonUiData>> = _selectedPokemons

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        _pokemonListUiState.value = _pokemonListUiState.value.copy(isLoading = true)
        viewModelScope.launch(coroutineDispatcher) {
            delay(3000)
            val result = repository.getPokemonList()
            if (result.isSuccess) {
                val pokemonListResult = result.getOrNull()
                _pokemonListUiState.value = if (pokemonListResult.isNullOrEmpty()) {
                    PokeListUiState(
                        isError = true,
                        isLoading = false,
                        errorMessage = "Empty request",
                        pokemonList = emptyList()
                    )
                } else {
                    PokeListUiState(
                        isError = false,
                        isLoading = false,
                        errorMessage = "Success",
                        pokemonList = pokemonListResult.map { pokeDto ->
                            PokemonUiData(
                                name = pokeDto.name,
                                image = pokeDto.image
                            )
                        }
                    )
                }
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                _pokemonListUiState.value = PokeListUiState(
                    isError = true,
                    isLoading = false,
                    errorMessage = errorMessage,
                    pokemonList = emptyList()
                )
            }
        }
    }


    fun toggleSelection(pokemon: PokemonUiData, isSelected: Boolean) {
        _selectedPokemons.value = if (isSelected) {
            if (_selectedPokemons.value.size < 2) {
                _selectedPokemons.value + pokemon
            } else {
                _selectedPokemons.value
            }
        } else {
            _selectedPokemons.value - pokemon
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val repository = (application as PokedexApplication).repository
                return PokeListViewModel(repository) as T
            }
        }
    }
}