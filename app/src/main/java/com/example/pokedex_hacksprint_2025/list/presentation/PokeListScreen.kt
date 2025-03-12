package com.example.pokedex_hacksprint_2025.list.presentation

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
import com.example.pokedex.R
import com.example.pokedex_hacksprint_2025.common.data.RetroFitClient
import com.example.pokedex_hacksprint_2025.common.model.PokemonApiResult
import com.example.pokedex_hacksprint_2025.common.model.PokemonItem
import com.example.pokedex_hacksprint_2025.detail.data.PokeDetailService
import com.example.pokedex_hacksprint_2025.detail.presentation.KEY_RESULT_POKEDEX
import com.example.pokedex_hacksprint_2025.detail.presentation.PokeDetailActivity
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
        Log.d("Click", "Clicked on the pokemon " + clickedPokemon.name)
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
    onClick: (PokemonItem) -> Unit
) {
    if (pokeListUiState.isError) {
        //Implementar a lógica pra tela de erro!

    } else if (pokeListUiState.isLoading) {
        //Implementar a lógica pra tela de loading!

    } else {
        val pokemonList: List<PokemonItem> = pokeListUiState.list.results
        PokemonList(pokemonList = pokemonList, onClick = onClick)
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonItem>,
    onClick: (PokemonItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) { // coluna implementada so para a visualizacao do retorno da api
        items(pokemonList) { pokeItem ->
            PokeCard(pokeItem, onClick = onClick)
        }
    }
}

@Composable
private fun PokeCard(
    pokemonItem: PokemonItem,
    onClick: (PokemonItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke(pokemonItem) }
    ) {
        if (LocalInspectionMode.current) { //Esse carinha aqui serve só pra fazer o preview, tá?
            Image(
                painter = painterResource(id = R.drawable.bulbasaur),
                contentDescription = pokemonItem.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        } else {
            //Transformando a url do pokemon numa imagem específica.
            val pokeImgUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        pokemonItem.url.trimEnd('/').split('/').last() +
                        ".png"

            AsyncImage(
                model = pokeImgUrl,
                contentDescription = pokemonItem.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
        }
        Text(pokemonItem.name)
    }
}

fun getPokemonDetails(url: Int) {
    val pokemonApi = RetroFitClient.retrofit.create(PokeDetailService::class.java)

    pokemonApi.getPokemon(url).enqueue(object : Callback<PokemonApiResult> {
        override fun onResponse(
            call: Call<PokemonApiResult>,
            response: Response<PokemonApiResult>
        ) {
            if (response.isSuccessful) {
                val pokemon = response.body()

                // Exibindo os tipos
                val types = pokemon?.types?.joinToString(", ") { it.type.name }

                // Exibindo as estatísticas
                val stats = pokemon?.stats?.joinToString(", ") {
                    "${it.stat.name}: ${it.baseStat}"
                }

                // Peso do Pokémon
                val weight = pokemon?.weight

                // URL da imagem oficial (front_default)
                val artworkUrl = pokemon?.sprites?.other?.officialArtwork?.frontDefault

                Log.d("MainActivity", "Types: $types")
                Log.d("MainActivity", "Stats: $stats")
                Log.d("MainActivity", "Weight: $weight")
                Log.d("MainActivity", "Official Artwork: $artworkUrl")  // Exibindo o URL da imagem
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

val bulbaMock = PokemonItem(
    name = "Bulbasaur",
    url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"
)