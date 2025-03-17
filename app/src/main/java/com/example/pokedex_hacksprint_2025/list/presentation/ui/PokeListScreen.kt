package com.example.pokedex_hacksprint_2025.list.presentation.ui

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
import android.content.Intent
import androidx.compose.runtime.Composable
import com.example.pokedex_hacksprint_2025.common.ui.ErrorScreen
import com.example.pokedex_hacksprint_2025.common.ui.LoadingScreen

@Composable
fun PokeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokeListViewModel
) {
    val context = LocalContext.current
    val pokeListUiState = viewModel.pokemonListUiState.collectAsState().value
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
            navController = navController
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
    navController: NavController
) {
    if (pokeListUiState.isError) {
        ErrorScreen(navController = navController)
    } else if (pokeListUiState.isLoading) {
        LoadingScreen()
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
                onClick = { onClick(pokeItem) }
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
                        )
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