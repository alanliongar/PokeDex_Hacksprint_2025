package com.example.pokedex_hacksprint_2025

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.battle.presentation.ui.BattleListScreen
import com.example.pokedex_hacksprint_2025.list.presentation.ui.PokeListScreen
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import com.example.pokedex_hacksprint_2025.ui.splash.PokeHomeSplashScreen

@Composable
fun PokedexApp(
    pokeListViewModel: PokeListViewModel,
    battleListViewModel: AIPokeBattleViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {
            PokeHomeSplashScreen(navController = navController)
        }
        composable(route = "pokemonList") {
            PokeListScreen(navController = navController, viewModel = pokeListViewModel)
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

            BattleListScreen(
                battleViewModel = battleListViewModel,
                pokeNameOne = pokeNameOne,
                pokeNameTwo = pokeNameTwo
            )
        }
    }
}
