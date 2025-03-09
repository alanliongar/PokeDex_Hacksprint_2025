package com.example.pokedex_hacksprint_2025
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex_hacksprint_2025.RetroFitClient.retrofit
import com.example.pokedex_hacksprint_2025.ui.theme.PokeDex_Hacksprint_2025Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    val pokemonApi = RetroFitClient.retrofit.create(PokemonApi::class.java)
    val pokemonNameUrl = pokemonApi.getPokemonList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDex_Hacksprint_2025Theme {
                val context = LocalContext.current
                var pokemonNameUrlList by remember { mutableStateOf<List<PokemonItem>>(emptyList()) }

                pokemonNameUrl.enqueue(object : Callback<PokemonListResponse> {
                    override fun onResponse(
                        call: Call<PokemonListResponse>,
                        response: Response<PokemonListResponse>
                    ) {
                        if (response.isSuccessful) {
                            val pokeinfo = response.body()?.results
                            if (pokeinfo != null) {
                                pokemonNameUrlList = pokeinfo
                                pokemonNameUrlList.map {
                                    getPokemonDetails(
                                        it.url.trimEnd('/').split('/').last().toInt()
                                    )
                                }
                            }
                        } else {
                            Log.d("MainActiviy", "RequestError :: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                        Log.d("MainActivity", "RequestFailed :: ${t.message}")
                    }
                })

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) { // coluna implementada so para a visualizacao do retorno da api
                    items(pokemonNameUrlList) { pokeItem->
                        PokeCard(pokeItem){clickedPokemon ->
                            Log.d("Clique ", "Clicou no pokemon "+clickedPokemon.name)
                            val intent = Intent(context, PokeDetailActivity::class.java)
                            intent.putExtra(KEY_RESULT_POKEDEX, clickedPokemon.name)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    fun getPokemonDetails(url: Int) {
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
}

@Composable
fun PokeCard(
    pokemonItem: PokemonItem,
    onClick: (PokemonItem)->Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable{onClick.invoke(pokemonItem)}
    ) {
        if (LocalInspectionMode.current) {
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
            val pokeImgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeCard(bulbaMock){ bulba->
        println("Bulba Clicked lol")
    }
}

val bulbaMock = PokemonItem(
    name = "Bulbasaur",
    url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"
)