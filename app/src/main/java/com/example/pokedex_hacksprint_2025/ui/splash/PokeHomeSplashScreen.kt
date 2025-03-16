package com.example.pokedex_hacksprint_2025.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.pokedex_hacksprint_2025.R
import kotlinx.coroutines.delay

@Composable
fun PokeHomeSplashScreen(
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("pokemonList") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.hello) // Seu GIF puro
                .decoderFactory(
                    if (android.os.Build.VERSION.SDK_INT >= 28) {
                        ImageDecoderDecoder.Factory()
                    } else {
                        GifDecoder.Factory()
                    }
                )
                .build(),
            contentDescription = "Splash GIF",
            modifier = Modifier.size(500.dp), // Apenas controla o tamanho
            contentScale = ContentScale.Fit // Mantém a proporção do GIF
        )
    }
}
