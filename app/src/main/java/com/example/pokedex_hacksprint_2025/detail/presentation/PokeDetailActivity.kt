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
import kotlinx.coroutines.launch

const val KEY_RESULT_POKEDEX = "DetailActivity_KEY_NAME"


class PokeDetailActivity : AppCompatActivity() {
    private val pokeDetailViewModel by viewModels<PokeDetailViewModel> { PokeDetailViewModel.Factory }

    // Variáveis que serão preenchidas quando os dados chegarem
    private var pokemonName: String = ""
    private var pokemonTypes: List<String> = emptyList()
    private var pokemonStats: List<Int> = emptyList()
    private var pokemonWeight: Int = 0
    private var pokemonArt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)

        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).orEmpty()

        // Chama a API
        pokeDetailViewModel.fetchPokemonDetail(pokeName)

        // Observa o ViewModel e preenche as variáveis
        lifecycleScope.launch {
            pokeDetailViewModel.pokemonDetail.collect { pokemonDetail ->
                if (pokemonDetail != null) {
                    // Atribui os valores às variáveis
                    pokemonName = pokeName.replaceFirstChar { it.uppercaseChar() }
                    pokemonTypes = pokemonDetail.types
                    pokemonStats = pokemonDetail.stats
                    pokemonWeight = pokemonDetail.weight ?: 0
                    pokemonArt = pokemonDetail.art ?: ""

                    updateUi() // Atualiza a UI com os valores das variáveis
                } else {
                    showError()
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
            .load(pokemonArt) // Usa a variável pokemonArt
            .into(pokemonImage)

        textName.text = pokemonName
        textWeight.text = "$pokemonWeight Kg"

        // Atualizando os tipos
        if (pokemonTypes.size > 1) {
            btnTypeOne.text = pokemonTypes[0]
            btnTypeTwo.text = pokemonTypes[1]
        } else if (pokemonTypes.isNotEmpty()) {
            btnTypeOne.text = pokemonTypes[0]
        }
    }

    private fun showError() {
        findViewById<TextView>(R.id.pokemonName).text = "Erro ao carregar Pokémon"
    }
}