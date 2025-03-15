package com.example.pokedex_hacksprint_2025.detail.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pokedex_hacksprint_2025.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val KEY_RESULT_POKEDEX = "DetailActivity_KEY_NAME"


class PokeDetailActivity : AppCompatActivity() {
    private val pokeDetailViewModel by viewModels<PokeDetailViewModel> { PokeDetailViewModel.Factory }

    private var pokemonName: String = ""
    private var pokemonTypes: List<String> = emptyList()
    private var pokemonStats: List<Int> = emptyList()
    private var pokemonWeight: Int = 0
    private var pokemonArt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)

        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).orEmpty()
        pokeDetailViewModel.fetchPokemonDetail(pokeName)

        lifecycleScope.launch(Dispatchers.Main) {
            pokeDetailViewModel.pokemonDetail.collect { pokemonDetailUiState ->
                if (pokemonDetailUiState?.isLoading == true) {
                    showLoading()
                } else if (pokemonDetailUiState?.isError == true) {
                    pokemonDetailUiState?.errorMessage?.let { showError(it) }
                } else {
                    if (pokemonDetailUiState?.pokemon != null) {
                        pokemonName = pokeName.replaceFirstChar { it.uppercaseChar() }
                        pokemonTypes = pokemonDetailUiState.pokemon.types
                        pokemonStats = pokemonDetailUiState.pokemon.stats
                        pokemonWeight = pokemonDetailUiState.pokemon.weight ?: 0
                        pokemonArt = pokemonDetailUiState.pokemon.art ?: ""
                        updateUi()
                    } else {
                        showError("Empty Requisition")
                    }
                }
            }
        }
    }

    private fun updateUi() {
        val pokemonImage = findViewById<ImageView>(R.id.pokemonImage)
        val textName = findViewById<TextView>(R.id.pokemonName)
        val textWeight = findViewById<TextView>(R.id.pokemonWeight)
        val btnTypeOne = findViewById<Button>(R.id.btnTypeOne)
        val btnTypeTwo = findViewById<Button>(R.id.btnTypeTwo)

        val hpBar = findViewById<ProgressBar>(R.id.hpBar)
        val attackBar = findViewById<ProgressBar>(R.id.attackBar)
        val defenseBar = findViewById<ProgressBar>(R.id.defenseBar)

        Glide.with(this)
            .load(pokemonArt)
            .into(pokemonImage)

        textName.text = pokemonName
        textWeight.text = "$pokemonWeight Kg"

        if (pokemonTypes.size == 2) {
            btnTypeOne.text = pokemonTypes[0]
            btnTypeTwo.text = pokemonTypes[1]
        } else if (pokemonTypes.isNotEmpty()) {
            btnTypeOne.text = pokemonTypes[0]
        }
    }

    private fun showLoading() {
        findViewById<TextView>(R.id.pokemonName).text = "Loading State"
    }

    private fun showError(errorMsg: String) {
        findViewById<TextView>(R.id.pokemonName).text = errorMsg
    }
}