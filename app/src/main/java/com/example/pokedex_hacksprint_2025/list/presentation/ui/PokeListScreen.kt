package com.example.pokedex_hacksprint_2025.list.presentation.ui

import OpenAiService
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.pokedex_hacksprint_2025.battle.data.remote.AIPokeBattleRemoteDataSource
import com.example.pokedex_hacksprint_2025.battle.presentation.AIPokeBattleViewModel
import com.example.pokedex_hacksprint_2025.battle.presentation.ui.BattleListScreen
import com.example.pokedex_hacksprint_2025.common.data.remote.RetrofitOpenAI
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_RESULT_POKEDEX
import com.example.pokedex_hacksprint_2025.detail.presentation.PokeDetailActivity
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import kotlinx.coroutines.selects.select

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
            selectedPokemons = selectedPokemons
        )

        // ✅ O botão aparece quando DOIS Pokémon forem selecionados
        if (selectedPokemons.size == 2) {
            Button(
                onClick = {
                    // Navegar para a tela de batalha, passando os Pokémon selecionados
                    navController.navigate("battle_screen" + "/${selectedPokemons[0].name}" + "/${selectedPokemons[1].name}")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(text = "⚔️ Check the battle!")
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
) {
    if (pokeListUiState.isError) {
        //Implementar a lógica pra tela de erro!

    } else if (pokeListUiState.isLoading) {
        //Implementar a lógica pra tela de loading!

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
    onSelectionChange: (PokemonUiData, Boolean) -> Unit, // Callback para atualizar seleção
    onClick: (PokemonUiData) -> Unit // Callback para navegar para detalhes
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clickable { onClick(pokemonUiData) } // Agora clica na imagem para navegar
        ) {
            // Imagem do Pokémon
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(id = R.drawable.bulbasaur),
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

            // Ícone de seleção sobreposto no canto superior direito
            Image(
                painter = painterResource(if (isSelected) R.drawable.sword_selected else R.drawable.sword_unselected),
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
        Text(pokemonUiData.name)
    }
}


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
)