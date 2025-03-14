package com.example.pokedex_hacksprint_2025.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.common.data.model.Pokemon
import com.example.pokedex_hacksprint_2025.common.data.remote.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonDetail
import com.example.pokedex_hacksprint_2025.detail.data.PokeDetailService
import com.example.pokedex_hacksprint_2025.detail.data.remote.PokemonDetailRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokeDetailViewModel(
    private val remote: PokemonDetailRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail: StateFlow<PokemonDetail?> = _pokemonDetail

    fun fetchPokemonDetail(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val result = remote.getPokemonDetails(name)
            if (result.isSuccess) {
                _pokemonDetail.value = result.getOrNull()
            } else {
                _pokemonDetail.value = null
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val service = RetroFitClient.retrofit.create(PokeDetailService::class.java)
                val remote = PokemonDetailRemoteDataSource(service)
                return PokeDetailViewModel(remote) as T
            }
        }
    }
}