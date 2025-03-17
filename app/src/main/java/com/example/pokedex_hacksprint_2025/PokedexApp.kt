package com.example.pokedex_hacksprint_2025

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.battle.presentation.ui.BattleScreen
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokeListScreen
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import com.example.pokedex_hacksprint_2025.ui.splash.PokeHomeSplashScreen
import com.example.pokedex_hacksprint_2025.ui.variations.ErrorScreen

@Composable
fun PokedexApp(
    pokeListViewModel: PokeListViewModel,
    battleListViewModel: AIPokeBattleViewModel
) {
    val navController = rememberNavController()

    // 🚀 Se a MainActivity foi chamada com navigateToPokemonList = true, força a navegação
    LaunchedEffect(MainActivity.navigateToPokemonList) {
        if (MainActivity.navigateToPokemonList) {
            navController.navigate("pokemonList") {
                popUpTo("splash") { inclusive = true } // Remove todas as telas anteriores
            }
            MainActivity.navigateToPokemonList = false // Resetar para evitar loops
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {
            PokeHomeSplashScreen(navController = navController)
        }
        composable(route = "pokemonList") {
            PokeListScreen(navController = navController, viewModel = pokeListViewModel)
        }
        composable(route = "errorScreen") {
            ErrorScreen(navController = navController)
        }
        composable(
            route = "battle_screen/{pokeNameOne}/{pokeNameTwo}",
            arguments = listOf(
                navArgument("pokeNameOne") { type = NavType.StringType },
                navArgument("pokeNameTwo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pokeNameOne = backStackEntry.arguments?.getString("pokeNameOne") ?: ""
            val pokeNameTwo = backStackEntry.arguments?.getString("pokeNameTwo") ?: ""

            BattleScreen(
                battleViewModel = battleListViewModel,
                pokeNameOne = pokeNameOne,
                pokeNameTwo = pokeNameTwo,
                navController = navController
            )
        }
    }
}

