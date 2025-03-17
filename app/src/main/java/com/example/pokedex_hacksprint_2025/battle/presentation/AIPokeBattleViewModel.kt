package com.example.pokedex_hacksprint_2025.battle.presentation

import OpenAiService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pokedex_hacksprint_2025.battle.data.remote.AIPokeBattleRemoteDataSource
import com.example.pokedex_hacksprint_2025.battle.presentation.ui.BattleUiState
import com.example.pokedex_hacksprint_2025.common.data.remote.RetrofitOpenAI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AIPokeBattleViewModel(
    private val remote: AIPokeBattleRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _pokemonBattleResult = MutableStateFlow<BattleUiState>(BattleUiState())
    val pokemonBattleResult: StateFlow<BattleUiState> = _pokemonBattleResult
    private var lastPokemonPair: Pair<String, String>? = null

    fun fetchBattleResult(firstPokemon: String, secondPokemon: String) {
        val currentPair = Pair(firstPokemon, secondPokemon)
        if (currentPair == lastPokemonPair)
            return
        _pokemonBattleResult.value = BattleUiState(isLoading = true)
        lastPokemonPair = currentPair
        viewModelScope.launch(coroutineDispatcher) {
            val battleResult =
                remote.battleResult(firstPokeName = firstPokemon, secondPokeName = secondPokemon)
            if (battleResult.isSuccess) {
                _pokemonBattleResult.value = BattleUiState(
                    battle = formatBattleText(battleResult.getOrNull() ?: "Erro ao gerar batalha!"),
                    isLoading = false,
                    isError = false
                )
            } else {
                _pokemonBattleResult.value =
                    BattleUiState(
                        battle = formatBattleText("Erro ao gerar batalha!"),
                        isError = true,
                        isLoading = false,
                        errorMessage = "Empty result!"
                    )
            }
        }
    }

    suspend private fun formatBattleText(rawText: String): AnnotatedString {
        return buildAnnotatedString {
            val regex = Regex("\\*\\*(.*?)\\*\\*") // Captura textos entre **negrito**
            var lastIndex = 0

            regex.findAll(rawText).forEach { match ->
                append(
                    rawText.substring(
                        lastIndex,
                        match.range.first
                    )
                )

                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(match.groupValues[1])
                pop()

                lastIndex = match.range.last + 1
            }

            if (lastIndex < rawText.length) {
                append(
                    rawText.substring(lastIndex, rawText.length)
                )
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