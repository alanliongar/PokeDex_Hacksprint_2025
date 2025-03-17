package com.example.pokedex_hacksprint_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import com.example.pokedex_hacksprint_2025.ui.theme.PokeDex_Hacksprint_2025Theme

class MainActivity : ComponentActivity() {
    private val pokeListViewModel by viewModels<PokeListViewModel> { PokeListViewModel.Factory }
    private val AIPokeBattleViewModel by viewModels<AIPokeBattleViewModel> { com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDex_Hacksprint_2025Theme {
                PokedexApp(
                    pokeListViewModel = pokeListViewModel,
                    battleListViewModel = AIPokeBattleViewModel
                )
            }
        }
    }
}
