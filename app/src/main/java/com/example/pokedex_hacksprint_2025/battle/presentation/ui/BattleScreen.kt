package com.example.pokedex_hacksprint_2025.battle.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.ui.variations.ErrorScreen
import com.example.pokedex_hacksprint_2025.ui.variations.LoadingScreen

@Composable
fun BattleScreen(
    modifier: Modifier = Modifier,
    battleViewModel: AIPokeBattleViewModel,
    pokeNameOne: String,
    pokeNameTwo: String,
    navController: NavController
) {
    battleViewModel.fetchBattleResult(pokeNameOne, pokeNameTwo)
    val battleUiState = battleViewModel.pokemonBattleResult.collectAsState().value
    BattleContent(modifier = modifier, battleUiState = battleUiState, navController = navController)
}

@Composable
private fun BattleContent(
    modifier: Modifier = Modifier,
    battleUiState: BattleUiState,
    navController: NavController
) {
    if (battleUiState.isError == true) {
        ErrorScreen(navController = navController)
    } else if (battleUiState.isLoading == true) {
        LoadingScreen()
    } else {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            battleUiState.battle?.let {
                Text(
                    text = it.text,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                )
            }
        }
    }
}