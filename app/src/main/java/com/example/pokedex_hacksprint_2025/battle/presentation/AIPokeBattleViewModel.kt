package com.example.pokedex_hacksprint_2025.battle.presentation

import OpenAiService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.battle.data.remote.AIPokeBattleRemoteDataSource
import com.example.pokedex_hacksprint_2025.common.data.remote.RetrofitOpenAI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AIPokeBattleViewModel(
    private val remote: AIPokeBattleRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _pokemonBattleResult = MutableStateFlow("")
    val pokemonBattleResult: StateFlow<String> = _pokemonBattleResult


    fun fetchBattleResult(firstPokemon: String, secondPokemon: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val battleResult =
                remote.battleResult(firstPokeName = firstPokemon, secondPokeName = secondPokemon)
            if (battleResult.isSuccess) {
                _pokemonBattleResult.value = battleResult.getOrNull() ?: "Erro ao gerar batalha!"
            } else {
                _pokemonBattleResult.value = "Erro ao gerar batalha!"
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val service: OpenAiService =
                    RetrofitOpenAI.instance.create(OpenAiService::class.java)
                val remote: AIPokeBattleRemoteDataSource = AIPokeBattleRemoteDataSource(service)
                return AIPokeBattleViewModel(remote) as T
            }
        }
    }
}