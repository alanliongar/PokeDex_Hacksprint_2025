package com.example.pokedex_hacksprint_2025.detail.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.common.data.remote.RetroFitClient
import com.example.pokedex_hacksprint_2025.detail.data.PokeDetailService
import com.example.pokedex_hacksprint_2025.detail.data.remote.PokemonDetailRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.graphics.Color
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.pokedex_hacksprint_2025.detail.presentation.ui.PokemonDetailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PokeDetailViewModel(
    private val remote: PokemonDetailRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<PokemonDetailUiState?>(null)
    val pokemonDetail: StateFlow<PokemonDetailUiState?> = _pokemonDetail

    fun fetchPokemonDetail(name: String) {
        _pokemonDetail.value = PokemonDetailUiState(isLoading = true)
        viewModelScope.launch(coroutineDispatcher) {
            delay(3000)
            val result = remote.getPokemonDetails(name)
            if (result.isSuccess) {
                _pokemonDetail.value = PokemonDetailUiState(
                    pokemon = result.getOrNull()
                )
                if (_pokemonDetail.value!!.pokemon?.types?.size == 2) {
                    _pokemonDetail.value = pokemonDetail.value?.copy(
                        btnColor = Pair(
                            getPokemonTypeColor(
                                _pokemonDetail.value!!.pokemon?.types?.get(0) ?: ""
                            ), getPokemonTypeColor(
                                _pokemonDetail.value!!.pokemon?.types?.get(1) ?: "Nao-valido"
                            )
                        )
                    )
                } else {
                    _pokemonDetail.value = pokemonDetail.value?.copy(
                        btnColor = Pair(
                            getPokemonTypeColor(
                                _pokemonDetail.value!!.pokemon?.types?.get(0) ?: ""
                            ), getPokemonTypeColor(
                                "Nao-valido"
                            )
                        )
                    )
                }
                println("Marieee " + _pokemonDetail.value?.pokemon?.height.toString())
            } else {
                _pokemonDetail.value =
                    PokemonDetailUiState(isError = true, errorMessage = "Requisition error")
            }
        }
    }

    suspend fun getDominantColorFromUrl(context: Context, imageUrl: String): Int? {
        return withContext(coroutineDispatcher) {
            try {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false) // Necessário para poder manipular o bitmap
                    .build()

                val result = (loader.execute(request) as? SuccessResult)?.drawable
                val bitmap = (result as? android.graphics.drawable.BitmapDrawable)?.bitmap

                bitmap?.let {
                    val palette = Palette.from(it).generate()
                    palette.dominantSwatch?.rgb // Retorna a cor predominante
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getPokemonTypeColor(type: String): Int {
        return when (type.lowercase()) {
            "grass" -> Color.parseColor("#78C850")  // Verde
            "fire" -> Color.parseColor("#F08030")  // Laranja queimado
            "water" -> Color.parseColor("#6890F0") // Azul médio
            "bug" -> Color.parseColor("#A8B820")// Verde amarelado
            "normal" -> Color.parseColor("#A8A878")// Cinza neutro
            "poison" -> Color.parseColor("#A040A0")// Roxo escuro
            "electric" -> Color.parseColor("#F8D030") // Amarelo vivo
            "ground" -> Color.parseColor("#E0C068")// Bege terroso
            "fairy" -> Color.parseColor("#EE99AC")// Rosa claro
            "fighting" -> Color.parseColor("#C03028") // Vermelho escuro
            "psychic" -> Color.parseColor("#F85888")// Rosa vibrante
            "rock" -> Color.parseColor("#B8A038")// Marrom amarelado
            "ghost" -> Color.parseColor("#705898")// Roxo escuro
            "ice" -> Color.parseColor("#98D8D8") // Azul claro
            "dragon" -> Color.parseColor("#7038F8") // Azul roxo
            "dark" -> Color.parseColor("#705848") // Marrom escuro
            "steel" -> Color.parseColor("#B8B8D0") // Cinza azulado
            "flying" -> Color.parseColor("#A890F0") // Lilás claro
            else -> Color.parseColor("#000000") // Preto (caso o tipo seja inválido)
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