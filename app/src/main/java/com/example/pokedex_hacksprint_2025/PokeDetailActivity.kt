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

        // Recebendo os dados via Intent
        val name = intent.getStringExtra("POKEMON_NAME") ?: getString(R.string.unknown)
        val weight = intent.getStringExtra("POKEMON_WEIGHT") ?: "0 KG"
        val height = intent.getStringExtra("POKEMON_HEIGHT") ?: "0 M"
        val hp = intent.getIntExtra("POKEMON_HP", 0)
        val attack = intent.getIntExtra("POKEMON_ATTACK", 0)
        val defense = intent.getIntExtra("POKEMON_DEFENSE", 0)

        // Atualizando os componentes da UI
        pokemonName.text = name
        pokemonWeight.text = getString(R.string.pokemon_weight_label, weight)
        pokemonHeight.text = getString(R.string.pokemon_height_label, height)

        hpBar.progress = hp
        attackBar.progress = attack
        defenseBar.progress = defense
    }
}