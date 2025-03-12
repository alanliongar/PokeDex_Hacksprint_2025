package com.example.pokedex_hacksprint_2025.list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.common.data.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.model.PokemonItem
import com.example.pokedex_hacksprint_2025.common.model.PokemonListResponse
import com.example.pokedex_hacksprint_2025.list.data.PokeListService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokeListViewModel(
    private val pokeListService: PokeListService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _pokemonListUiState =
        MutableStateFlow<PokeListUiState>(PokeListUiState())
    val pokemonListUiState: StateFlow<PokeListUiState> = _pokemonListUiState

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        _pokemonListUiState.value = _pokemonListUiState.value.copy(isLoading = true)
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val response =
                    pokeListService.getPokemonList().execute() // Executa de forma síncrona
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _pokemonListUiState.value = PokeListUiState(
                            isError = false,
                            isLoading = false,
                            errorMessage = "Success",
                            list = PokemonListResponse(
                                results = response.body()?.results ?: emptyList()
                            )
                        )
                    } else {
                        _pokemonListUiState.value = PokeListUiState(
                            isError = true,
                            isLoading = false,
                            errorMessage = "Empty request",
                            list = PokemonListResponse(results = emptyList())
                        )
                    }
                } else {
                    Log.e("MainActivity", "Erro na requisição: ${response.errorBody()}")
                    _pokemonListUiState.value = PokeListUiState(
                        isError = true,
                        isLoading = false,
                        errorMessage = "Requisition error: ${response.errorBody()}",
                        list = PokemonListResponse(results = emptyList())
                    )
                }
            } catch (ex: Exception) {
                Log.e("MainActivity", "Falha na requisição", ex)
                _pokemonListUiState.value = PokeListUiState(
                    isError = true,
                    isLoading = false,
                    errorMessage = "Falha na requisição: ${ex.message}",
                    list = PokemonListResponse(results = emptyList())
                )
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val pokeListService = RetroFitClient.retrofit.create(PokeListService::class.java)
                return PokeListViewModel(pokeListService) as T
            }
        }
    }
}