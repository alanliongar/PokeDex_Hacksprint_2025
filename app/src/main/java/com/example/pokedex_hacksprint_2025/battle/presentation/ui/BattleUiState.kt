package com.example.pokedex_hacksprint_2025.battle.presentation.ui

import androidx.compose.ui.text.AnnotatedString

data class BattleUiState(
    val battle: AnnotatedString? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
)