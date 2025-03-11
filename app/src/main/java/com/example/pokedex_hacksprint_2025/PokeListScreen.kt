package com.example.pokedex_hacksprint_2025

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.pokedex.R

@Composable
fun PokeListScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var pokemonNameUrlList by remember { mutableStateOf<List<PokemonItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        pokemonNameUrlList = getPokemonList()
    }
    PokeListContent(context = context)
}


@Composable
private fun PokeListContent(modifier: Modifier = Modifier, context: Context) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) { // coluna implementada so para a visualizacao do retorno da api
        items(pokemonNameUrlList) { pokeItem->
            PokeCard(pokeItem){clickedPokemon ->
                Log.d("Click", "Clicked on the pokemon "+clickedPokemon.name)
                val intent = Intent(context, PokeDetailActivity::class.java)
                intent.putExtra(KEY_RESULT_POKEDEX, clickedPokemon.name)
                startActivity(intent)
            }
        }
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
