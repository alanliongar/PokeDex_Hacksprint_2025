package com.example.pokedex_hacksprint_2025.battle.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel

@Composable
fun BattleListScreen(
    modifier: Modifier = Modifier,
    battleViewModel: AIPokeBattleViewModel,
    pokeNameOne: String,
    pokeNameTwo: String
) {
    battleViewModel.fetchBattleResult(pokeNameOne, pokeNameTwo)
    val pokemonBattleResult = battleViewModel.pokemonBattleResult.collectAsState().value
    BattleListContent(modifier = modifier, pokemonBattleResult)
}

@Composable
private fun BattleListContent(
    modifier: Modifier = Modifier,
    battleResult: String,
) {
    Text(text = battleResult)
}

@Preview(showBackground = true)
private fun BattleListScreenPreview() {
    //BattleListContent()
}