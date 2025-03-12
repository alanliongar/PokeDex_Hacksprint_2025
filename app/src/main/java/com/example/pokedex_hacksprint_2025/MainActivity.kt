package com.example.pokedex_hacksprint_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokedex_hacksprint_2025.ui.theme.PokeDex_Hacksprint_2025Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDex_Hacksprint_2025Theme {
                val context = LocalContext.current
                PokeListScreen(context = context)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeCard(bulbaMock) { bulba ->
        println("Bulba Clicked lol")
    }
}

val bulbaMock = PokemonItem(
    name = "Bulbasaur",
    url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"
)