package com.example.pokedex_hacksprint_2025.ui.variations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex_hacksprint_2025.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        supportActionBar?.hide()
    }
}
