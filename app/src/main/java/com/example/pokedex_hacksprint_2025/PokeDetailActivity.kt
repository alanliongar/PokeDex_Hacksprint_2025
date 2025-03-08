package com.example.pokedex_hacksprint_2025

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R

class PokeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_detail)

        // Pegando os componentes da UI
        val pokemonImage = findViewById<ImageView>(R.id.pokemonImage)
        val pokemonName = findViewById<TextView>(R.id.pokemonName)
        val pokemonWeight = findViewById<TextView>(R.id.pokemonWeight)
        val pokemonHeight = findViewById<TextView>(R.id.pokemonHeight)

        val hpBar = findViewById<ProgressBar>(R.id.hpBar)
        val attackBar = findViewById<ProgressBar>(R.id.attackBar)
        val defenseBar = findViewById<ProgressBar>(R.id.defenseBar)


    }
}