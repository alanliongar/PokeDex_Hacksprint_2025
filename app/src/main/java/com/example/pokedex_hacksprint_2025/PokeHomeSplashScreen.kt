package com.example.pokedex_hacksprint_2025

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun PokeHomeSplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(100) // Aguarda 2 segundos
        navController.navigate("pokemonList") {
            popUpTo("splash") { inclusive = true } // Remove a Splash Screen da pilha de navegação
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.eevee1), // Substitua pelo seu logo
            contentDescription = "Splash Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}