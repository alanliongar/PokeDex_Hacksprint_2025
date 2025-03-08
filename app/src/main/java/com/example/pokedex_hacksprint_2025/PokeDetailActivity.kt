package com.example.pokedex_hacksprint_2025

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R

const val KEY_RESULT_POKEDEX = "DetailActivity_KEY_NAME"


class PokeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)
        val pokeName: String = intent.getStringExtra(KEY_RESULT_POKEDEX).toString()
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
        pokemonName.text = pokeName?:"Eeve"
        pokemonWeight.text = "20.0 KG"
        pokemonHeight.text="1.5 M"
        btnTypeOne.text="Fly"
        btnTypeTwo.text="Fire"
        btnTypeOne.setBackgroundColor(getColor(R.color.teal_700))
        btnTypeTwo.setBackgroundColor(getColor(R.color.teal_200))
    }
}