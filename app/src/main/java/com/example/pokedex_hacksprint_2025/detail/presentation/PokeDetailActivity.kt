package com.example.pokedex_hacksprint_2025.detail.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    private var pokemonHeight: Int = 0
    private var pokemonArt: String = ""
    private var btnColors: Pair<Int, Int> = Pair(0, 0)
    private var pokemonArtColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)
        supportActionBar?.hide()
        val dispatcher = Dispatchers.Main
        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).orEmpty()
        pokeDetailViewModel.fetchPokemonDetail(pokeName)

        lifecycleScope.launch(dispatcher) {
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
                        pokemonHeight = pokemonDetailUiState.pokemon.height ?: 0
                        pokemonArt = pokemonDetailUiState.pokemon.art ?: ""
                        btnColors = pokemonDetailUiState.btnColor ?: Pair(0, 0)
                        pokemonArtColor =
                            pokeDetailViewModel.getDominantColorFromUrl(this@PokeDetailActivity, pokemonArt) ?: 0
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
        val textHeight = findViewById<TextView>(R.id.pokemonHeight)
        val btnTypeOne = findViewById<Button>(R.id.btnTypeOne)
        val btnTypeTwo = findViewById<Button>(R.id.btnTypeTwo)
        val imgCard = findViewById<View>(R.id.image_background)

        val hpBar = findViewById<ProgressBar>(R.id.hpBar)
        val attackBar = findViewById<ProgressBar>(R.id.attackBar)
        val defenseBar = findViewById<ProgressBar>(R.id.defenseBar)

        Glide.with(this)
            .load(pokemonArt)
            .into(pokemonImage)

        imgCard.backgroundTintList = ColorStateList.valueOf(pokemonArtColor)


        textName.text = pokemonName
        textWeight.text = "${String.format("%.1f", pokemonWeight / 10f).replace(",", ".")} Kg"
        textHeight.text = "${String.format("%.1f", pokemonHeight / 10f).replace(",", ".")} M"

        if (pokemonTypes.size == 2) {
            btnTypeOne.text = pokemonTypes[0]
            btnTypeOne.setBackgroundColor(btnColors.first)
            btnTypeTwo.text = pokemonTypes[1]
            btnTypeTwo.setBackgroundColor(btnColors.second)
        } else if (pokemonTypes.isNotEmpty()) {
            btnTypeOne.text = pokemonTypes[0]
            btnTypeOne.setBackgroundColor(btnColors.first)
            btnTypeTwo.isVisible = false
        }
    }

    private fun showLoading() {
        findViewById<TextView>(R.id.pokemonName).text = "Loading State"
    }

    private fun showError(errorMsg: String) {
        findViewById<TextView>(R.id.pokemonName).text = errorMsg
    }
}