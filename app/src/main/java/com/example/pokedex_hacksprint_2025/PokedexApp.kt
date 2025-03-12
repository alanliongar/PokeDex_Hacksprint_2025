package com.example.pokedex_hacksprint_2025

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListScreen
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel

@Composable
fun PokedexApp(
    pokeListViewModel: PokeListViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "pokemonList"){
        composable(route = "pokemonList"){
            PokeListScreen(navController = navController, viewModel = pokeListViewModel)
        }
    }
}