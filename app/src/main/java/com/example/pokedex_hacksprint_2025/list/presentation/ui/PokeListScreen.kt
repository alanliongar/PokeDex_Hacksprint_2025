package com.example.pokedex_hacksprint_2025.list.presentation.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex_hacksprint_2025.R
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_RESULT_POKEDEX
import com.example.pokedex_hacksprint_2025.detail.presentation.PokeDetailActivity
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import com.example.pokedex_hacksprint_2025.ui.variations.ErrorActivity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.pokedex_hacksprint_2025.ui.variations.IsLoading
import com.example.pokedex_hacksprint_2025.ui.variations.LoadingActivity
import kotlinx.coroutines.delay

@Composable
fun PokeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokeListViewModel
) {
    val context = LocalContext.current

    val pokeListUiState = viewModel.pokemonListUiState.collectAsState().value
    println(pokeListUiState.toString() + "Alann")
    val selectedPokemons = viewModel.selectedPokemons.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        PokeListContent(
            pokeListUiState = pokeListUiState,
            modifier = modifier,
            onClick = { clickedPokemon ->
                Log.d("Click", "Clicked on the pokemon ${clickedPokemon.name}")
                val intent = Intent(context, PokeDetailActivity::class.java).apply {
                    putExtra(KEY_RESULT_POKEDEX, clickedPokemon.name)
                }
                context.startActivity(intent)
            },
            onSelectionChange = { pokemon, isSelected ->
                viewModel.toggleSelection(pokemon, isSelected)
            },
            selectedPokemons = selectedPokemons,
            context = context
        )

        if (selectedPokemons.size == 2) {
            IconButton(
                onClick = {
                    navController.navigate("battle_screen/${selectedPokemons[0].name}/${selectedPokemons[1].name}")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .size(130.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.sword_battle_dark else R.drawable.sword_battle),
                    contentDescription = "Check the battle",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}


@Composable
private fun PokeListContent(
    pokeListUiState: PokeListUiState,
    modifier: Modifier = Modifier,
    selectedPokemons: List<PokemonUiData>,
    onClick: (PokemonUiData) -> Unit,
    onSelectionChange: (PokemonUiData, Boolean) -> Unit,
    context: Context
) {
    if (pokeListUiState.isError) {
        context.startActivity(Intent(context, ErrorActivity::class.java))
    } else if (pokeListUiState.isLoading) {
        //Era pra iniciar a activity_loading.xml, mas por conta da complexidade envolvida na volta
        //Colocamos a activity da Mariana adaptada numa função compose, por ser algo simples
        //E tornar fácil a atualização do estado de loading para o estado de sucesso.
        IsLoading()
    } else {
        val pokemonList: List<PokemonUiData> = pokeListUiState.pokemonList
        PokemonList(
            pokemonList = pokemonList,
            onClick = onClick,
            onSelectionChange = onSelectionChange,
            selectedPokemons = selectedPokemons
        )
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonUiData>,
    selectedPokemons: List<PokemonUiData>,
    onClick: (PokemonUiData) -> Unit,
    onSelectionChange: (PokemonUiData, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para armazenar os Pokémon selecionados (máximo de 2)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemonList) { pokeItem ->
            PokeCard(
                pokemonUiData = pokeItem,
                isSelected = selectedPokemons.contains(pokeItem),
                onSelectionChange = onSelectionChange,
                onClick = { onClick(pokeItem) } // Passa o callback para navegação
            )
        }
    }
}


@Composable
private fun PokeCard(
    pokemonUiData: PokemonUiData,
    isSelected: Boolean,
    onSelectionChange: (PokemonUiData, Boolean) -> Unit,
    onClick: (PokemonUiData) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clickable { onClick(pokemonUiData) }
        ) {
            // Imagem do Pokémon
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(id = R.drawable.floragato),
                    contentDescription = pokemonUiData.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AsyncImage(
                    model = pokemonUiData.image,
                    contentDescription = pokemonUiData.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Image(
                painter = painterResource(
                    if (isSelected) {
                        if (isSystemInDarkTheme()) R.drawable.sword_selected_dark else R.drawable.sword_selected
                    } else {
                        if (isSystemInDarkTheme()) R.drawable.sword_unselected_dark else R.drawable.sword_unselected
                    }
                ),
                contentDescription = "Selecionado",
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .clickable {
                        onSelectionChange(
                            pokemonUiData,
                            !isSelected
                        ) // Controla a seleção ao clicar na espada
                    }
            )
        }
        Text(
            text = pokemonUiData.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeCard(
        bulbaMock, isSelected = false,
        onSelectionChange = TODO()
    ) { bulba ->
        println("Bulba Clicked lol")
    }
}

val bulbaMock = PokemonUiData(
    name = "Bulbasaur",
    image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"
)*/
