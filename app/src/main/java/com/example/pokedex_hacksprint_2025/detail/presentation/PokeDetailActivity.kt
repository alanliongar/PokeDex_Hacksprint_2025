package com.example.pokedex_hacksprint_2025.detail.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pokedex_hacksprint_2025.R
import com.example.pokedex_hacksprint_2025.detail.presentation.ui.PokemonDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val KEY_RESULT_POKEDEX = "DetailActivity_KEY_NAME"

class PokeDetailActivity : AppCompatActivity() {
    private val pokeDetailViewModel by viewModels<PokeDetailViewModel> { PokeDetailViewModel.Factory }
    private lateinit var viewGroupPokeDetail: ConstraintLayout
    private lateinit var viewGroupLoading: ConstraintLayout
    private lateinit var viewGroupError: ConstraintLayout
    private var pokemonArtColor: Int = 0
    private var btnColors: Pair<Int, Int> = Pair(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)
        supportActionBar?.hide()

        // Inicializa os ViewGroups
        viewGroupPokeDetail = findViewById<ConstraintLayout>(R.id.viewGroup_Poke_Detail)
        viewGroupLoading = findViewById<ConstraintLayout>(R.id.viewGroup_loading)
        viewGroupError = findViewById<ConstraintLayout>(R.id.viewGroup_error)

        // Começa no estado de Loading
        showLoading()

        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).orEmpty()
        pokeDetailViewModel.fetchPokemonDetail(pokeName)

        lifecycleScope.launch(Dispatchers.Main) {
            pokeDetailViewModel.pokemonDetail.collect { pokemonDetailUiState ->
                when {
                    pokemonDetailUiState?.isLoading == true -> showLoading()
                    pokemonDetailUiState?.isError == true -> showError()
                    pokemonDetailUiState?.pokemon != null -> updateUi(
                        pokemonDetailUiState = pokemonDetailUiState,
                        pokeName = pokeName
                    )

                    else -> showError()
                }
            }
        }
    }

    private fun showLoading() {
        viewGroupPokeDetail.visibility = View.GONE
        viewGroupError.visibility = View.GONE
        viewGroupLoading.visibility = View.VISIBLE

        val imageView = findViewById<ImageView>(R.id.splashScreenPikachuGif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading_pikachu)
            .into(imageView)
    }

    private fun showError() {
        viewGroupPokeDetail.visibility = View.GONE
        viewGroupLoading.visibility = View.GONE
        viewGroupError.visibility = View.VISIBLE
    }

    private fun updateUi(pokemonDetailUiState: PokemonDetailUiState, pokeName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dominantColor = pokemonDetailUiState.pokemon?.art?.let {
                pokeDetailViewModel.getDominantColorFromUrl(this@PokeDetailActivity, it)
            } ?: 0

            val types = pokemonDetailUiState.pokemon?.types ?: emptyList()
            val firstTypeColor =
                if (types.isNotEmpty()) pokeDetailViewModel.getPokemonTypeColor(types[0]) else 0
            val secondTypeColor =
                if (types.size > 1) pokeDetailViewModel.getPokemonTypeColor(types[1]) else firstTypeColor

            withContext(Dispatchers.Main) {
                pokemonArtColor = dominantColor
                btnColors = Pair(firstTypeColor, secondTypeColor)

                // Agora a UI é atualizada após as cores estarem corretas
                viewGroupLoading.visibility = View.GONE
                viewGroupError.visibility = View.GONE
                viewGroupPokeDetail.visibility = View.VISIBLE

                val pokemon = pokemonDetailUiState.pokemon

                val pokemonImage = findViewById<ImageView>(R.id.pokemonImage)
                val textName = findViewById<TextView>(R.id.pokemonName)
                val textWeight = findViewById<TextView>(R.id.pokemonWeight)
                val textHeight = findViewById<TextView>(R.id.pokemonHeight)
                val btnTypeOne = findViewById<Button>(R.id.btnTypeOne)
                val btnTypeTwo = findViewById<Button>(R.id.btnTypeTwo)
                val imgCard = findViewById<View>(R.id.image_background)
                val hpTextView = findViewById<TextView>(R.id.hpTextView)
                val atkTextView = findViewById<TextView>(R.id.atkTextView)
                val defenseTextView = findViewById<TextView>(R.id.defenseTextView)
                val expTextView = findViewById<TextView>(R.id.expTextView)

                val hpBar = findViewById<ProgressBar>(R.id.hpBar)
                val attackBar = findViewById<ProgressBar>(R.id.attackBar)
                val defenseBar = findViewById<ProgressBar>(R.id.defenseBar)
                val expBar = findViewById<ProgressBar>(R.id.expBar)

                if (pokemon != null) {
                    Glide.with(this@PokeDetailActivity)
                        .load(pokemon.art)
                        .into(pokemonImage)

                    imgCard.backgroundTintList = ColorStateList.valueOf(pokemonArtColor)

                    textName.text = pokeName
                    textWeight.text =
                        "${String.format("%.1f", pokemon.weight!! / 10f).replace(",", ".")} Kg"
                    textHeight.text =
                        "${String.format("%.1f", pokemon.height!! / 10f).replace(",", ".")} M"

                    if (pokemon.types.size == 2) {
                        btnTypeOne.text = pokemon.types[0]
                        btnTypeOne.setBackgroundColor(btnColors.first)
                        btnTypeTwo.text = pokemon.types[1]
                        btnTypeTwo.setBackgroundColor(btnColors.second)
                    } else {
                        btnTypeOne.text = pokemon.types.getOrNull(0) ?: ""
                        btnTypeOne.setBackgroundColor(btnColors.first)
                        btnTypeTwo.isVisible = pokemon.types.size > 1
                    }

                    hpBar.progress = pokemon.stats.getOrNull(0) ?: 0
                    attackBar.progress = pokemon.stats.getOrNull(1) ?: 0
                    defenseBar.progress = pokemon.stats.getOrNull(2) ?: 0
                    expBar.progress = pokemon.stats.getOrNull(3) ?: 0

                    hpTextView.text = "${pokemon.stats.getOrNull(0) ?: 0}/300"
                    atkTextView.text = "${pokemon.stats.getOrNull(1) ?: 0}/300"
                    defenseTextView.text = "${pokemon.stats.getOrNull(2) ?: 0}/300"
                    expTextView.text = "${((pokemon.stats.getOrNull(3) ?: 0) * 2.4).toInt()}/1000"
                }
            }
        }
    }

}
