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
import com.example.pokedex_hacksprint_2025.common.data.remote.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonApiResult
import com.example.pokedex_hacksprint_2025.common.data.remote.model.PokemonDetail
import com.example.pokedex_hacksprint_2025.detail.data.PokeDetailService
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_POKEMON_ART
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_POKEMON_STAT
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_POKEMON_TYPE
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_POKEMON_WEIGHT
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_RESULT_POKEDEX
import com.example.pokedex_hacksprint_2025.detail.presentation.PokeDetailActivity
import com.example.pokedex_hacksprint_2025.list.presentation.PokeListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        getPokemonDetails(pokemonName) { pokemonDetail ->
            if (pokemonDetail != null) {
                Log.d("PokeListScreen", "Types: ${pokemonDetail.types}")
                Log.d("PokeListScreen", "Stats: ${pokemonDetail.stats}")
                Log.d("PokeListScreen", "Weight: ${pokemonDetail.weight}")
                Log.d("PokeListScreen", "Art: ${pokemonDetail.art}")

                val intent = Intent(context, PokeDetailActivity::class.java).apply {
                    putExtra(KEY_RESULT_POKEDEX, clickedPokemon.name)
                    putStringArrayListExtra(
                        KEY_POKEMON_TYPE,
                        ArrayList(pokemonDetail.types)
                    )
                    putIntegerArrayListExtra(
                        KEY_POKEMON_STAT,
                        ArrayList(pokemonDetail.stats)
                    )
                    putExtra(KEY_POKEMON_WEIGHT, pokemonDetail.weight)
                    putExtra(KEY_POKEMON_ART, pokemonDetail.art)
                }
                context.startActivity(intent)
            } else {
                Log.e("Click", "Failed to fetch Pokemon details")
            }
        }
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

fun getPokemonDetails(name: String, onResult: (PokemonDetail?) -> Unit) {
    val pokemonApi = RetroFitClient.retrofit.create(PokeDetailService::class.java)

    pokemonApi.getPokemon(name).enqueue(object : Callback<PokemonApiResult> {
        override fun onResponse(
            call: Call<PokemonApiResult>,
            response: Response<PokemonApiResult>
        ) {
            if (response.isSuccessful) {
                val pokemon = response.body()

                // Processando os dados da API
                val types = pokemon?.types?.map { it.type.name } ?: emptyList()
                val stats = pokemon?.stats?.map { it.baseStat } ?: emptyList()
                val weight = pokemon?.weight ?: 0
                val artworkUrl = pokemon?.sprites?.other?.officialArtwork?.frontDefault ?: ""

                val pokemonDetail = PokemonDetail(types, stats, weight, artworkUrl)

                // Passando o resultado para quem chamou a função
                onResult(pokemonDetail)
            } else {
                Log.d("MainActivity", "RequestError :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<PokemonApiResult>, t: Throwable) {
            Log.d("MainActivity", "RequestFailed :: ${t.message}")
        }
    })
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