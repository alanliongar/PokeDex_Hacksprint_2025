package com.example.pokedex_hacksprint_2025

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokedex_hacksprint_2025.RetroFitClient.retrofit
import com.example.pokedex_hacksprint_2025.ui.theme.PokeDex_Hacksprint_2025Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDex_Hacksprint_2025Theme {
                var pokemonNameUrlList by remember { mutableStateOf<List<PokemonItem>>(emptyList()) }

                val pokemonApi = RetroFitClient.retrofit.create(PokemonApi::class.java)
                val pokemonNameUrl = pokemonApi.getPokemonList()

                pokemonNameUrl.enqueue(object : Callback<PokemonListResponse>{
                    override fun onResponse(
                        call: Call<PokemonListResponse>,
                        response: Response<PokemonListResponse>
                    ) {
                        if (response.isSuccessful){
                            val pokeinfo = response.body()?.results
                            if (pokeinfo != null) {
                                pokemonNameUrlList = pokeinfo
                            }
                        } else {
                            Log.d("MainActiviy", "RequestError :: ${response.errorBody()}")
                        }
                    }
                    override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })




                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                LazyColumn { // coluna implementada so para a visualizacao do retorno da api
                    items(pokemonNameUrlList){
                        Text(text = it.name)
                        Text(text = it.url)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeDex_Hacksprint_2025Theme {
        Greeting("Android")
    }
}