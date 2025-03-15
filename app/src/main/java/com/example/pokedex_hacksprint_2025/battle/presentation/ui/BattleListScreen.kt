package com.example.pokedex_hacksprint_2025.battle.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    battleResult: AnnotatedString
) {
    Box(
        modifier = Modifier
            .fillMaxSize(), // Ocupa toda a tela
        contentAlignment = Alignment.Center // Centraliza o conteúdo no meio
    ) {
        Text(
            text = battleResult,
            fontSize = 20.sp,
            textAlign = TextAlign.Center, // Centraliza o texto horizontalmente
            modifier = Modifier
                .fillMaxWidth() // Ocupa toda a largura disponível
                .fillMaxHeight(0.7f) // Ocupa 70% da altura disponível
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun BattleListScreenPreview() {
    //BattleListContent()
}