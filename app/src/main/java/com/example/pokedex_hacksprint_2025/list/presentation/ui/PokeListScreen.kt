package com.example.pokedex_hacksprint_2025.list.presentation.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex_hacksprint_2025.R
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_RESULT_POKEDEX
import com.example.pokedex_hacksprint_2025.detail.presentation.PokeDetailActivity
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel

@Composable
fun PokeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokeListViewModel
) {
    val context = LocalContext.current
    val pokeListUiState = viewModel.pokemonListUiState.collectAsState().value

    PokeListContent(
        pokeListUiState = pokeListUiState,
        modifier = modifier
    ) { clickedPokemon ->
        Log.d("Click", "Clicked on the pokemon ${clickedPokemon.name}")

        val pokemonName = clickedPokemon.name

        // Obtendo os detalhes de forma assíncrona
        val intent = Intent(context, PokeDetailActivity::class.java).apply {
            putExtra(KEY_RESULT_POKEDEX, clickedPokemon.name)
        }
        context.startActivity(intent)
    }
}


@Composable
private fun PokeListContent(
    pokeListUiState: PokeListUiState,
    modifier: Modifier = Modifier,
    onClick: (PokemonUiData) -> Unit
) {
    if (pokeListUiState.isError) {
        //Implementar a lógica pra tela de erro!

    } else if (pokeListUiState.isLoading) {
        //Implementar a lógica pra tela de loading!

    } else {
        val pokemonList: List<PokemonUiData> = pokeListUiState.pokemonList
        PokemonList(pokemonList = pokemonList, onClick = onClick)
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonUiData>,
    onClick: (PokemonUiData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemonList) { pokeItem ->
            PokeCard(pokeItem, onClick = onClick)
        }
    }
}

@Composable
private fun PokeCard(
    pokemonUiData: PokemonUiData,
    onClick: (PokemonUiData) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke(pokemonUiData) }
    ) {
        if (LocalInspectionMode.current) { //Esse carinha aqui serve só pra fazer o preview, tá?
            Image(
                painter = painterResource(id = R.drawable.bulbasaur),
                contentDescription = pokemonUiData.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        } else {
            AsyncImage(
                model = pokemonUiData.image,
                contentDescription = pokemonUiData.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        }
        Text(pokemonUiData.name)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeCard(bulbaMock) { bulba ->
        println("Bulba Clicked lol")
    }
}

val bulbaMock = PokemonUiData(
    name = "Bulbasaur",
    image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"
)