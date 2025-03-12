package com.example.pokedex_hacksprint_2025.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.common.data.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.model.PokemonItem
import com.example.pokedex_hacksprint_2025.list.data.PokeListService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokeListViewModel(
    private val pokeListService: PokeListService,
) : ViewModel() {
    private val _pokemonList =
        MutableStateFlow<List<PokemonItem>>(emptyList())
    val pokemonList: StateFlow<List<PokemonItem>> = _pokemonList

    init {
        viewModelScope.launch {//Aqui não defino dispatcher por que na função já tem o dispatcher definido.
            _pokemonList.value = getPokemonList(pokemonApi = pokeListService)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val pokeListService = RetroFitClient.retrofit.create(PokeListService::class.java)
                return PokeListViewModel(pokeListService) as T
            }
        }
    }

}