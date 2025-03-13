package com.example.pokedex_hacksprint_2025.detail.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.capitalize
import com.example.pokedex.R

const val KEY_RESULT_POKEDEX = "DetailActivity_KEY_NAME"
const val KEY_POKEMON_TYPE = "DetailActivity_type"
const val KEY_POKEMON_STAT = "DetailActivity_stat"
const val KEY_POKEMON_WEIGHT = "DetailActivity_weigh"
const val KEY_POKEMON_ART = "DetailActivity_art"


class PokeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)
        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).toString()
        val pokeTypes: ArrayList<String> = intent.getStringArrayListExtra(KEY_POKEMON_TYPE) ?: arrayListOf()
        val pokeStats: ArrayList<Int> = intent.getIntegerArrayListExtra(KEY_POKEMON_STAT) ?: arrayListOf()
        val pokeWeight: Int = intent.getIntExtra(KEY_POKEMON_WEIGHT, 0)
        val pokeArt: String = intent.getStringExtra(KEY_POKEMON_ART) ?: ""

        Log.d("PokeDetailActivity", "Received Name: $pokeName")
        Log.d("PokeDetailActivity", "Received Types: ${pokeTypes.joinToString()}")
        Log.d("PokeDetailActivity", "Received Stats: ${pokeStats.joinToString()}")
        Log.d("PokeDetailActivity", "Received Weight: $pokeWeight")
        Log.d("PokeDetailActivity", "Received Artwork: $pokeArt")

// Evitar erro ao acessar índices:
        if (pokeTypes.isNotEmpty() && pokeStats.isNotEmpty()) {
                    Log.d("PokeDetailActivity", "First Type: ${pokeTypes.get(0)}")
                    Log.d("PokeDetailActivity", "First Stat: ${pokeStats.get(0)}")
                } else {
                    Log.d("PokeDetailActivity", "No Types or Stats available")
                }

        // Pegando os componentes da UI
        val pokemonImage = findViewById<ImageView>(R.id.pokemonImage)
        val pokemonName = findViewById<TextView>(R.id.pokemonName)
        val pokemonWeight = findViewById<TextView>(R.id.pokemonWeight)
        val pokemonHeight = findViewById<TextView>(R.id.pokemonHeight)
        val btnTypeOne = findViewById<Button>(R.id.btnTypeOne)
        val btnTypeTwo = findViewById<Button>(R.id.btnTypeTwo)

        val hpBar = findViewById<ProgressBar>(R.id.hpBar)
        val attackBar = findViewById<ProgressBar>(R.id.attackBar)
        val defenseBar = findViewById<ProgressBar>(R.id.defenseBar)

        //Dados mockado
        pokemonImage.setImageResource(R.drawable.eevee1)
        pokemonName.text = pokeName.replaceFirstChar { it.uppercaseChar() }
        pokemonWeight.text = pokeWeight.toString() + "Kg"
        pokemonHeight.text="1.5 M"
        if (pokeTypes.size > 1) {
            btnTypeOne.text= pokeTypes[0]
            btnTypeTwo.text= pokeTypes[1]
        } else {
            btnTypeOne.text= pokeTypes[0]
        }



        btnTypeOne.setBackgroundColor(getColor(R.color.teal_700))
        btnTypeTwo.setBackgroundColor(getColor(R.color.teal_200))
    }
}